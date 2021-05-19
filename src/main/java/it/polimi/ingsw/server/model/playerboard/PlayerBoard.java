package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;

import java.util.*;
/**
 * This class represents the PlayerBoard of a Player.
 *
 * @see WareHouse
 * @see Slot
 * @see StrongBox
 */
public class PlayerBoard {

    private final StrongBox strongBox;
    private final WareHouse wareHouse;
    private final List<Slot> slotList;
    private final List<ProductionPower> extraProductionPowers = new ArrayList<>();
    private VirtualView observer;

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     */
    public PlayerBoard() {
        this.strongBox = new StrongBox();
        this.wareHouse = new WareHouse();
        this.slotList = new ArrayList<>();

        this.slotList.add( new Slot());
        this.slotList.add( new Slot());
        this.slotList.add( new Slot());
    }

    public WareHouse getWareHouse() { return wareHouse; }

    public List<Slot> getSlotList() { return slotList; }

    public StrongBox getStrongBox() { return strongBox; }

    public List<ProductionPower> getExtraProductionPowers() {
        return extraProductionPowers;
    }

    /**
     *
     * @param developmentCard is the <code>DevelopmentCard</code> the player tries to add
     * @param idSlot represents the number of the <code>Slot</code> in which to insert the <code>DevelopmentCard</code>
     * @return true if the IllegalArgumentException is not called, false if the developmentCard does not pass the checks in the idSlot Slot
     */
    public boolean canAddCardInSlot(DevelopmentCard developmentCard, int idSlot){
        return slotList.get(idSlot).canAddCard(developmentCard);
    }

    public void addCardInSlot(DevelopmentCard developmentCard, int idSlot){
        slotList.get(idSlot).addCard(developmentCard);
    }

    /**
     *
     * @param resourceRequirement contains the <code>Resources</code> to be controlled in the <code>WareHouse</code> / <code>StrongBox</code>
     * @return true if the amount of required resources is present in the <code>Depot</code> or <code>WareHouse</code>
     */
    public boolean isAffordable(ResourceRequirement resourceRequirement){

        return ( strongBox.getQuantity(resourceRequirement.getResource()) +
                    wareHouse.totalResourcesOfAType(resourceRequirement.getResource()) ) >= resourceRequirement.getQuantity();
    }

    /**
     * Removes first from the WareHouse and then if in the WareHouse there aren't enough Resources of the required type , removes from StrongBox
     *
     * @param resourceRequirement represents the <code>Resources</code> to be removed / paid from the strongbox or warehouse
     * to buy a <code>DevelopmentCard</code> or activate production power
     */
    public void pay(ResourceRequirement resourceRequirement){
        if(!isAffordable(resourceRequirement)) throw new IllegalArgumentException("The player cannot proceed with the payment cause he doesn't have enough Resources");

        boolean resourceInWareHouse = true;
        int i = 0;

        while( i < resourceRequirement.getQuantity() ) {

            if(resourceInWareHouse) {
                try {
                    wareHouse.removeResource(resourceRequirement.getResource());
                    i++;
                }
                catch (IllegalArgumentException e) {
                    resourceInWareHouse = false;
                }
            }
            else {
                strongBox.removeResource(resourceRequirement.getResource());
                i++;
            }
        }
    }


    /**
     *
     * @return the total number of points collected from the <code>>Resources</code> and victory points of the <code>Cards</code>
     */
    public int countVictoryPoints(){
        int sumCardPoints = 0;

        for(Slot slot : this.slotList ) sumCardPoints+= slot.countCardPoints();

        return this.strongBox.countTotalResources()
                + this.wareHouse.countTotalResources() + sumCardPoints;
    }

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
     *                                          and a <code>ProductionPower</code> that contains the same fixed <code>Resources</code>
     *                                          of the corresponding power, plus the agnostic <code>Resource</code>s converted in concrete ones.
     *
     * @see ProductionPower
     */

    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){

        ProductionPower totalProductionPower;
        if(canActivateProduction(selectedCardIds, selectedExtraPowers)) {
            totalProductionPower = getTotalProductionPower(selectedCardIds, selectedExtraPowers);
            for (Resource r : totalProductionPower.getInput().keySet())
                pay(new ResourceRequirement(r, totalProductionPower.getInput().get(r)));
        }
        else{
            throw new IllegalArgumentException("Cannot activate production");
        }
        for(Resource r: totalProductionPower.getOutput().keySet())
            strongBox.addResource(r, totalProductionPower.getOutput().get(r));
    }

    public boolean canActivateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        ProductionPower totalProductionPower;
        try{
            totalProductionPower = getTotalProductionPower(selectedCardIds, selectedExtraPowers);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }
        for(Map.Entry<Resource, Integer> entry : totalProductionPower.getInput().entrySet())
            if (!isAffordable(new ResourceRequirement(entry.getKey(), entry.getValue())))
                return false;
        return true;
    }

    private ProductionPower getTotalProductionPower(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        Map<Resource, Integer> totalInput = new EnumMap<>(Resource.class);
        Map<Resource, Integer> totalOutput = new EnumMap<>(Resource.class);
        ProductionPower power;

        for(int i : selectedCardIds){

            try{
                power = Card.getById(i, getLastDevCards()).getPower();
            }
            catch (ElementNotFoundException e){
                throw new IllegalArgumentException("The user requested a production he didn't have");
            }

            incrementMap(totalInput, power.getInput());
            incrementMap(totalOutput, power.getOutput());
        }

        for(Integer i : selectedExtraPowers.keySet()){

            power = extraProductionPowers.get(i);

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
     * A support method to quickly retrieve a list containing the first element of the 3 development card slots.
     *
     * @return                         A <code>List</code> with the first element of each of the <code>Slot</code>s
     */
    private List<DevelopmentCard> getLastDevCards(){

        List<DevelopmentCard> lastDevCards = new ArrayList<>();
        for (Slot s : slotList){
            if(!s.isEmpty())
                lastDevCards.add(s.getLastCard());
        }
        return lastDevCards;
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
                power.getAgnosticOutput() == chosenInput.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Adds a new possibly agnostic <code>ProductionPower</code> to the <code>extraProductionPower</code>
     *
     * @param productionPower           The power to add
     */
    public void addExtraProductionPower(ProductionPower productionPower) {

        extraProductionPowers.add(productionPower);
        // if it's the base production power, the client game is not yet initialized so we can't update
        if (extraProductionPowers.size() != 1)
            observer.extraPowerUpdate(productionPower);
    }

    public void setObserver(VirtualView view){
        observer = view;
        wareHouse.setObserver(observer);
        strongBox.setObserver(observer);
        for(Slot slot : slotList){
            slot.setObserver(view);
        }
    }
}

