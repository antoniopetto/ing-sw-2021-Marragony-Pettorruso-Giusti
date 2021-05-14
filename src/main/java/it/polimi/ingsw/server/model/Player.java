package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.update.WhiteMarbleAliasUpdateMsg;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.cards.LeaderCard;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.playerboard.*;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.server.model.shared.MarketBoard;
import it.polimi.ingsw.server.model.shared.PopeFavourTile;

import java.util.*;

public class Player extends AbstractPlayer{

    private final String username;

    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard();
    private final Map<Resource, Integer> activeDiscounts = new EnumMap<>(Resource.class);
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private final List<LeaderCard> leaderCardList = new ArrayList<>();
    private VirtualView observer;

    /**
     * <code>Player</code> constructor.
     * Gives the player three <code>PopeFavourTile</code>, of value 2, 3 and 4 respectively.
     *
     * @param username      The player username
     */
    public Player(String username){
        this.username = username;
        tiles.add(new PopeFavourTile(2));
        tiles.add(new PopeFavourTile(3));
        tiles.add(new PopeFavourTile(4));
    }

    /**
     * Counts all the victory points the player has gained until now in the game.
     *
     * @return      The total sum of victory points
     */
    public int countPoints(){
        int counter = 0;
        for (PopeFavourTile t : tiles){
            counter += t.isGained() ? t.getValue() : 0;
        }
        counter += playerBoard.countVictoryPoints();
        counter += super.getPosition().getVictoryPoints();
        for (LeaderCard card:leaderCardList) {
            if(card.isPlayed())
                counter+=card.getVictoryPoints();
        }
        return counter;
    }

    /**
     * Adds a <code>Resource</code> discount to the set of discounts of the player.
     *
     * @param resource      The discounted resource
     * @param amount        The entity of the discount
     */
    public void addActiveDiscount(Resource resource, int amount) {
        activeDiscounts.put(resource, amount);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    /**
     * Adds a <code>Resource</code> to the set of white marble aliases of the player.
     *
     * @param resource      The white marble alias
     */
    public void addWhiteMarbleAlias(Resource resource) {
        whiteMarbleAliases.add(resource);
        observer.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
    }

    public void clearWhiteMarbleAlias(){
        whiteMarbleAliases.clear();
        observer.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
    }

    public void addAllWhiteMarbleAlias(){
        whiteMarbleAliases.add(Resource.STONE);
        whiteMarbleAliases.add(Resource.SERVANT);
        whiteMarbleAliases.add(Resource.SHIELD);
        whiteMarbleAliases.add(Resource.COIN);
        observer.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
    }

    /**
     * This method is used to play a Leader card
     *
     * @param cardId is the id of the card to ble played, which can be 1 or 0
     * @return true if the card is playable and therefore played
     */
    public boolean playLeaderCard(int cardId) throws ElementNotFoundException {

        if(cardId == 0) throw new IllegalArgumentException("There are no leader cards");
        LeaderCard card = Card.getById(cardId, leaderCardList);
        if(card.isPlayed())
            throw new IllegalStateException("Card already played");
        if(card.isPlayable(this)) {
            card.play(this);
            return true;
        }
        else return false;
    }

    /**
     * Removes a <code>LeaderCard</code> from the player's list
     *
     * @param id                            The card id
     * @throws ElementNotFoundException     If the if doesn't correspond to any of the players cards
     */
    public void removeLeaderCard(int id) throws ElementNotFoundException {
        if(id == 0) throw new IllegalArgumentException("All leaderCards have already been discarded");
            else {
            leaderCardList.remove(Card.getById(id, leaderCardList));
            observer.discardLeaderCardUpdate(id);
        }
    }

    /**
     * Adds a <code>DevelopmentCard</code> in a player's slot
     *
     * @param card      The card to add
     * @param slotId    The destination slot
     */
    public void addCard(DevelopmentCard card, int slotId){
        if (canBuyCard(card) && playerBoard.canAddCardInSlot(card, slotId)){
            for (ResourceRequirement req : card.getDiscountedRequirements(activeDiscounts)){
                playerBoard.pay(req);
            }
            playerBoard.addCardInSlot(card, slotId);
            observer.addCardInSlotUpdate(card.getId(), slotId);
        }
        else throw new IllegalArgumentException("Invalid move: can't pay or can't place the selected Development Card");
    }

    /**
     * Checks it the player can buy a certain <code>DevelopmentCard</code>.
     *
     * @param card      The card to check
     * @return          <code>true</code> if the player has enough resources, <code>false</code> otherwise
     */
    public boolean canBuyCard(DevelopmentCard card) {

        for (ResourceRequirement req : card.getDiscountedRequirements(activeDiscounts)) {
            if (!req.isSatisfied(this))
                return false;
        }
        return true;
    }

    /**
     * Buys a row/column from the <code>MarketBoard</code>.
     *
     * @param marketBoard       Reference to the game's marketboard
     * @param idLine            Index of the selected row/column
     * @param isRow             <code>true</code> if we buy a row, <code>false</code> otherwise
     * @return                  The retrieved list of marbles
     */
    public List<Marble> buyResources(MarketBoard marketBoard, int idLine , boolean isRow){
        List<Marble> marbleList;
        if (isRow)
            marbleList = marketBoard.buyRow(idLine);
        else
            marbleList = marketBoard.buyColumn(idLine);

        return marbleList;
    }

    /**
     * The effect of a vatican report. For a <code>Player</code> consists in gaining a <code>VaticanReportTile</code>
     *
     * @param tileNumber        The tile to activate
     */
    @Override
    public void vaticanReportEffect(int tileNumber) {

        tiles.get(tileNumber).gain();
    }

    public List<LeaderCard> getLeaderCardList(){
        return new ArrayList<>(leaderCardList);
    }

    public void setObserver(VirtualView view){
        observer = view;
        playerBoard.setObserver(observer);
    }

    public void setLeaderCards(List<LeaderCard> cards){
        leaderCardList.addAll(cards);
    }

    public String getUsername() { return username; }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Map<Resource, Integer> getActiveDiscount() {
        return activeDiscounts;
    }
}
