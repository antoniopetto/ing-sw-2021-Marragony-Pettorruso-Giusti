package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.cards.LeaderCard;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.playerboard.*;
import it.polimi.ingsw.shared.Marble;
import it.polimi.ingsw.server.model.shared.MarketBoard;
import it.polimi.ingsw.shared.PopeFavourTile;
import it.polimi.ingsw.shared.Card;
import it.polimi.ingsw.shared.ProductionPower;
import it.polimi.ingsw.shared.Resource;

import java.util.*;
import java.util.stream.Collectors;

public class Player extends AbstractPlayer {

    private transient VirtualView virtualView;
    private final String username;
    private final List<PopeFavourTile> tiles = List.of(new PopeFavourTile(2),
                                                       new PopeFavourTile(3),
                                                       new PopeFavourTile(4));
    private final Set<Resource> activeDiscounts = new HashSet<>();
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private final List<LeaderCard> leaderCardList = new ArrayList<>();
    private final PlayerBoard playerBoard;


    /**
     * <code>Player</code> constructor.
     * Gives the player three <code>PopeFavourTile</code>, of value 2, 3 and 4 respectively.
     *
     * @param username      The player username
     */
    public Player(String username){
        this.username = username;
        this.playerBoard = new PlayerBoard();
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
        playerBoard.setVirtualView(virtualView);
    }

    public SimplePlayer getSimple(){

        SimplePlayer player = new SimplePlayer(username);
        player.setPosition(getPosition().getNumber());
        player.setWarehouse(playerBoard.getWareHouse().getSimple());
        player.setSlots(playerBoard.getSlotList().stream().map(Slot::getSimple).collect(Collectors.toList()));
        player.setStrongbox(playerBoard.getStrongBox().getContent());
        player.setLeaderCards(leaderCardList.stream().map(LeaderCard::getSimple).collect(Collectors.toList()));
        player.setExtraProductionPowers(playerBoard.getExtraProductionPowers());
        player.setActiveDiscounts(activeDiscounts);
        player.setTiles(tiles);
        return player;
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
     */
    public void addActiveDiscount(Resource resource) {
        activeDiscounts.add(resource);
        virtualView.cardDiscountUpdate(username, resource);
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
        virtualView.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
    }

    public void clearWhiteMarbleAlias(){
        whiteMarbleAliases.clear();
        virtualView.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
    }

    public void addAllWhiteMarbleAlias(){
        whiteMarbleAliases.add(Resource.STONE);
        whiteMarbleAliases.add(Resource.SERVANT);
        whiteMarbleAliases.add(Resource.SHIELD);
        whiteMarbleAliases.add(Resource.COIN);
        virtualView.whiteMarbleAliasUpdate(username, whiteMarbleAliases);
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
            virtualView.playLeaderCardUpdate(cardId);
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

        leaderCardList.remove(Card.getById(id, leaderCardList));
        virtualView.discardLeaderCardUpdate(id);
    }

    /**
     * Adds a <code>DevelopmentCard</code> in a player's slot
     *
     * @param card      The card to add
     * @param slotIdx    The destination slot
     */
    public void addCard(DevelopmentCard card, int slotIdx){
        if (canBuyCard(card) && playerBoard.canAddCardInSlot(card, slotIdx)){
            for (ResourceRequirement req : card.getDiscountedRequirements(activeDiscounts)){
                playerBoard.pay(req);
            }
            playerBoard.addCardInSlot(card, slotIdx);
            virtualView.addCardInSlotUpdate(card.getId(), slotIdx);
        }
        else throw new IllegalArgumentException("Invalid move: can't pay or can't place the selected Development Card");
    }

    public int countDevCards(){
        int count = 0;
        for (Slot slot : playerBoard.getSlotList()){
            count += slot.getDevelopmentCardList().size();
        }
        return count;
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
     * @param marketBoard       Reference to the game's marketBoard
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
     * @param tileIdx        The tile to activate
     */
    @Override
    public void vaticanReportEffect(int tileIdx) {

        tiles.get(tileIdx).gain();
        virtualView.tileGainedUpdate(username, tileIdx);
    }

    public List<LeaderCard> getLeaderCardList(){
        return new ArrayList<>(leaderCardList);
    }

    public void setLeaderCards(List<LeaderCard> cards){
        leaderCardList.addAll(cards);
    }

    public String getUsername() { return username; }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Set<Resource> getActiveDiscount() {
        return activeDiscounts;
    }

    public List<PopeFavourTile> getTiles(){ return new ArrayList<>(tiles); }

    /**
     * This method implements the logic behind the action of activating the production in a player's turn.
     * It receives from the user all the elements to identify what <code>ProductionPower</code>, regular or special,
     * he wants to activate. Moreover, in association with the <code>id</code> of every special <code>ProductionPower</code>,
     * it requires a <code>ProductionPower</code> that represents all of its agnostic <code>Resources</code> after being determined.
     * The method first checks if the player can afford to activate these effects all at once, and if so proceeds.
     *
     * @param selectedCardIds                   The <code>Set</code> of <code>DevelopmentCard id</code>s that the user has selected.
     *
     * @param selectedExtraPowers               A <code>Map</code> linking <code>id</code> and desired composition of a special <code>ProductionPower</code>.
     *                                          The <code>Map</code> stores an <code>Integer</code> representing the <code>id</code>,
     *                                          and a <code>ProductionPower</code> the agnostic <code>Resource</code>s
     *                                          converted in concrete ones.
     *
     * @return                                  The number of positions to advance
     * @see ProductionPower
     */

    public int activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){

        ProductionPower totalProductionPower;
        if(canActivateProduction(selectedCardIds, selectedExtraPowers)) {
            totalProductionPower = getTotalProductionPower(selectedCardIds, selectedExtraPowers);
            for (Resource r : totalProductionPower.getInput().keySet())
                playerBoard.pay(new ResourceRequirement(r, totalProductionPower.getInput().get(r)));
        }
        else{
            throw new IllegalArgumentException("Cannot activate production");
        }
        int faithCounter = 0;
        for(Resource r : totalProductionPower.getOutput().keySet()) {
            if (r == Resource.FAITH)
                faithCounter += totalProductionPower.getOutput().get(Resource.FAITH);
            else
                playerBoard.getStrongBox().addResource(r, totalProductionPower.getOutput().get(r));
        }
        return faithCounter;
    }

    private boolean canActivateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        ProductionPower totalProductionPower;
        try{
            totalProductionPower = getTotalProductionPower(selectedCardIds, selectedExtraPowers);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }
        for(Map.Entry<Resource, Integer> entry : totalProductionPower.getInput().entrySet())
            if (!playerBoard.isAffordable(new ResourceRequirement(entry.getKey(), entry.getValue())))
                return false;
        return true;
    }

    private ProductionPower getTotalProductionPower(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        Map<Resource, Integer> totalInput = new EnumMap<>(Resource.class);
        Map<Resource, Integer> totalOutput = new EnumMap<>(Resource.class);
        ProductionPower power;

        for(int i : selectedCardIds){

            try{
                power = Card.getById(i, playerBoard.getLastDevCards()).getPower();
            } catch (ElementNotFoundException e){
                throw new IllegalArgumentException("The user requested a production he didn't have");
            }
            incrementMap(totalInput, power.getInput());
            incrementMap(totalOutput, power.getOutput());
        }

        for(Integer i : selectedExtraPowers.keySet()){

            power = playerBoard.getExtraProductionPowers().get(i);

            incrementMap(totalInput, power.getInput());
            incrementMap(totalOutput, power.getOutput());

            ProductionPower chosenResources = selectedExtraPowers.get(i);

            if (!specialProductionConsistent(power, chosenResources))
                throw new IllegalArgumentException("The client choice of special production is illegal");

            incrementMap(totalInput, chosenResources.getInput());
            incrementMap(totalOutput, chosenResources.getOutput());
        }

        return new ProductionPower(totalInput, totalOutput);
    }

    /**
     * Support method that increments partial sum of input/output resources with the <code>values</code> of new <code>Map</code>s.
     *
     * @param totalMap        The <code>Resource</code> map to be incremented.
     * @param map             The input <code>Resource</code> map to be added.
     */
    private void incrementMap(Map<Resource, Integer> totalMap, Map<Resource, Integer> map) {

        for(Resource resource : map.keySet())
            totalMap.compute(resource, (k, v) -> (v == null) ? map.get(resource) : v + map.get(resource));
    }

    /**
     * Checks that the agnostic <code>Resources</code> for a certain <code>ProductionPower</code> sent by the client are legal.
     * @param power             The original <code>ProductionPower</code>.
     * @param chosenResources       The <code>Resources</code> chosen to replace the agnostic ones.
     * @return                  true iff the conversion is legal.
     */
    private boolean specialProductionConsistent(ProductionPower power, ProductionPower chosenResources) {

        if (chosenResources.getAgnosticInput() != 0 || chosenResources.getAgnosticOutput() != 0)
            return false;
        var chosenInput = chosenResources.getInput();
        var chosenOutput = chosenResources.getOutput();

        return !chosenInput.containsKey(Resource.FAITH) && !chosenOutput.containsKey(Resource.FAITH) &&
                power.getAgnosticInput() == chosenInput.values().stream().mapToInt(Integer::intValue).sum() &&
                power.getAgnosticOutput() == chosenOutput.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Set<Integer> getLeaderCardIds(){
        Set<Integer> ids = new HashSet<>();
        getLeaderCardList().forEach(i -> ids.add(i.getId()));
        return ids;
    }
}
