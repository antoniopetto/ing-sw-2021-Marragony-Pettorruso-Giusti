package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.ProductionPower;
import it.polimi.ingsw.model.exceptions.NotAffordableException;
import it.polimi.ingsw.model.playerboard.*;
import it.polimi.ingsw.model.shared.Marble;
import it.polimi.ingsw.model.shared.Position;

import java.util.*;

public class Player {

    private int idSlotSelect;
    private final String username;
    private Position position;
    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard();
    private final Set<Resource> activeDiscount = new HashSet<>();
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private final Set<Resource> extraProductionPower = new HashSet<>(); //può andare nella Playerboard
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
        counter += position.getVictoryPoints();
        for (LeaderCard card:leaderCardList) {
            if(card.isPlayed())
                counter+=card.getVictoryPoints();
        }
        return counter;
    }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Set<Resource> getActiveDiscount() {
        return activeDiscount;
    }

    public void addActiveDiscount(Resource resource) {
        activeDiscount.add(resource);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    public void addWhiteMarbleAlias(Resource resource) {
        whiteMarbleAliases.add(resource);
    }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

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


    public boolean tryBuyDevelopmentCard(DevelopmentCard developmentCard){
        return developmentCard.getRequirement().isSatisfied(this);
    }

    public boolean tryToAddDevelopmentCard(DevelopmentCard developmentCard){

        boolean isPossibleAddCard = true;

        try {
            this.playerBoard.addCard(developmentCard, getIdSlotSelect());

        }catch (IllegalArgumentException e){
            isPossibleAddCard = true;
        }

        return isPossibleAddCard;

    }

    public void selectIdSlot(int idSlot){
        this.idSlotSelect = idSlot;
    }

    public int getIdSlotSelect() {
        return idSlotSelect;
    }

    public void buyResources(Game game, int idLine , boolean isRow, DepotName depotName ){
        List<Marble> marbleList;
        if(isRow) marbleList = game.getMarketBoard().getRow(idLine);
        else marbleList = game.getMarketBoard().getColumn(idLine);

        for(Marble marble : marbleList){
            if(marble.getResource()!= null){
                while (!playerBoard.getWareHouse().isInsertable(depotName,marble.getResource())){
                    System.out.println("it is not possible to place this resource in the warehouse");
                    //cambio del depotName da parte dell'utente
                    //possibile prove di depotSwitch da parte dell'utente
                }
                //utente decide di scartare la risorsa o la risorsa viene scartata in automatico dopo un tot?
            }
        }
    }
}
