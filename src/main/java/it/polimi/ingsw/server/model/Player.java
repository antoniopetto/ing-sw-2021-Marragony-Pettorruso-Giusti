package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.cards.LeaderCard;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.playerboard.*;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.server.model.shared.PopeFavourTile;

import java.util.*;

public class Player extends AbstractPlayer{

    private final String username;

    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard();
    private final Map<Resource, Integer> activeDiscounts = new EnumMap<>(Resource.class);
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private VirtualView observer;

    public Player(String username){
        this.username = username;
        tiles.add(new PopeFavourTile(2));
        tiles.add(new PopeFavourTile(3));
        tiles.add(new PopeFavourTile(4));
    }

    public void setLeaderCards(List<LeaderCard> cards){
        leaderCardList.addAll(cards);
    }

    public void removeLeaderCard(int id) throws ElementNotFoundException {
        leaderCardList.remove(Card.getById(id, leaderCardList));
    }

    public String getUsername() { return username; }

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

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Map<Resource, Integer> getActiveDiscount() {
        return activeDiscounts;
    }

    public void addActiveDiscount(Resource resource, int amount) {
        activeDiscounts.put(resource, amount);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    public void addWhiteMarbleAlias(Resource resource) {
        whiteMarbleAliases.add(resource);
    }

    /**
     * This method is used to play a Leader card
     * @param cardId is the id of the card to ble played, which can be 1 or 0
     * @return true if the card is playable and therefore played
     */
    public boolean playLeaderCard(int cardId) throws ElementNotFoundException {

        LeaderCard card = Card.getById(cardId, leaderCardList);
        if(card.isPlayed()) throw new IllegalStateException("Card already played");
        if(card.isPlayable(this))
        {
            card.play(this);
            return true;
        }
        else return false;
    }

    /**
     * This method is used to discard a leader card
     * @param cardId is the id of the card to discard
     */
    public void discardLeaderCard(int cardId) throws ElementNotFoundException {
        Card.getById(cardId, leaderCardList);
        leaderCardList.remove(cardId);
        observer.discardLeaderCardUpdate(cardId);
    }

    public void addCard(DevelopmentCard card, int idSlot){
        if (canBuyCard(card) && playerBoard.canAddCardInSlot(card, idSlot)){
            for (ResourceRequirement req : card.getDiscountedRequirements(activeDiscounts)){
                playerBoard.pay(req);
            }
            playerBoard.addCardInSlot(card, idSlot);
        }
        else throw new IllegalArgumentException("Invalid move: can't pay or can't place the selected Development Card");
    }

    public boolean canBuyCard(DevelopmentCard card) {

        for (ResourceRequirement req : card.getDiscountedRequirements(activeDiscounts)) {
            if (!req.isSatisfied(this))
                return false;
        }
        return true;
    }

    public List<Marble> buyResources(Game game, int idLine , boolean isRow){
        List<Marble> marbleList;
        if(isRow) marbleList = game.getMarketBoard().getRow(idLine);
        else marbleList = game.getMarketBoard().getColumn(idLine);

        return marbleList;
    }

    @Override
    public void vaticanReportEffect(int tileNumber) {
        tiles.get(tileNumber).gain();
    }
}
