package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.NotAffordableException;
import it.polimi.ingsw.model.playerboard.*;
import it.polimi.ingsw.model.shared.Marble;

import java.util.*;

public class Player extends AbstractPlayer{

    private int idSlotSelect;
    private final String username;

    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard();
    private final Map<Resource, Integer> activeDiscounts = new EnumMap<>(Resource.class);
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private List<LeaderCard> leaderCardList;


    Player(String username){
        this.username = username;
        tiles.add(new PopeFavourTile(2));
        tiles.add(new PopeFavourTile(3));
        tiles.add(new PopeFavourTile(4));

    }

    public void setLeaderCardList(List<LeaderCard> leaderCardList) {
        this.leaderCardList = leaderCardList;
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
    public boolean playLeaderCard(int cardId)
    {
        if(cardId!=0&&cardId!=1) throw new IllegalArgumentException("Invalid id number");
        LeaderCard card = leaderCardList.get(cardId);
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
    public void discardLeaderCard(int cardId)
    {
        if(cardId!=0&&cardId!=1) throw new IllegalArgumentException("Invalid id number");
        leaderCardList.remove(cardId);
    }

    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers) throws NotAffordableException {
        playerBoard.activateProduction(selectedCardIds, selectedExtraPowers);
    }

    public boolean tryBuyCard(DevelopmentCard developmentCard) {

        boolean isPossible = false;
        int newQuantityThenDiscount = 0;

        //DevelopmentCard developmentCard = developmentCardDecks.readTop(cardColor, level); -->DA SPOSTARE NEL CONTROLLER
        // passare nel metodo direttamente la carta ( in questo caso spostare questo metodo nel player)

        List<ResourceRequirement> resourceRequirement = developmentCard.getRequirements();
        List<ResourceRequirement> resourceRequirementDiscount = new ArrayList<>();

        for( ResourceRequirement resourceRequirement1 : resourceRequirement ){
            newQuantityThenDiscount = resourceRequirement1.getQuantity();

            for (Resource resource : this.getActiveDiscount().keySet()) {
                if (resourceRequirement1.getResource().equals(resource)) {
                    if (newQuantityThenDiscount >= this.getActiveDiscount().get(resource))
                        newQuantityThenDiscount = -this.getActiveDiscount().get(resource);
                    else throw new IllegalArgumentException("Unable to remove more resources than are requested in the requirements");
                }
            }

            resourceRequirementDiscount.add(new ResourceRequirement(resourceRequirement1.getResource(), newQuantityThenDiscount));
        }

        for( ResourceRequirement resourceRequirementDiscount1 : resourceRequirementDiscount){
            isPossible = resourceRequirementDiscount1.isSatisfied(this);
            if(!isPossible) return false;
        }

        //Come gestire il discorso del Pay? Come far arrivare al metodo pay la nuova lista di resourceRequirement (resourceRequirementDiscount)?

        return isPossible;
    }

    public boolean tryAddDevelopmentCardInSlot(DevelopmentCard developmentCard){
        return tryBuyCard(developmentCard) && this.playerBoard.tryAddCard(developmentCard, getIdSlotSelect());
    }

    public void selectIdSlot(int idSlot){
        this.idSlotSelect = idSlot;
    }

    public int getIdSlotSelect() {
        return idSlotSelect;
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

    @Override
    public void activateEndGame() {

    }


}
