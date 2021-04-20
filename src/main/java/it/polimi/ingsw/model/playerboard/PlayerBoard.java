package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.model.exceptions.NotAffordableException;
import it.polimi.ingsw.model.shared.Identifiable;

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

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     *
     */
    public PlayerBoard() {
        this.strongBox = new StrongBox();
        this.wareHouse = new WareHouse();
        this.slotList = new ArrayList<>();
        //creare metodo ausiliario per la creazione degli slot
        this.slotList.add( new Slot(0));
        this.slotList.add( new Slot(1));
        this.slotList.add( new Slot(2));
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
                    wareHouse.removeResourcefromWareHouse(resourceRequirement.getResource());
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
     * @throws NotAffordableException           When at least one <code>Resource</code> cannot be afforded the entire <code>PlayerBoard</code>.
     * @see ProductionPower
     */

    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers) throws NotAffordableException {

        Map<Resource, Integer> totalInput = new EnumMap<>(Resource.class);
        Map<Resource, Integer> totalOutput = new EnumMap<>(Resource.class);
        ProductionPower power;

        for(Integer i : selectedCardIds){

            try{
                power = findById(i, getLastDevCards()).getPower();
            }
            catch (ElementNotFoundException e){
                throw new IllegalArgumentException("The user requested a production he didn't have");
            }

            incrementMap(totalInput, power.getInputResources());
            incrementMap(totalInput, power.getOutputResources());
        }

        for(Integer i : selectedExtraPowers.keySet()){

            power = extraProductionPowers.get(i);

            incrementMap(totalInput, power.getInputResources());
            incrementMap(totalOutput, power.getOutputResources());

            ProductionPower chosenResources = selectedExtraPowers.get(i);

            if (!specialProductionConsistent(power, chosenResources))
                throw new IllegalArgumentException("The client choice of special production is illegal");

            incrementMap(totalInput, chosenResources.getInputResources());
            incrementMap(totalOutput, chosenResources.getOutputResources());
        }

        for(Resource r : totalInput.keySet())
            if (isAffordable(new ResourceRequirement(r, totalInput.get(r))))
                throw new NotAffordableException();

        for(Resource r : totalInput.keySet())
            pay(new ResourceRequirement(r, totalInput.get(r)));

        for(Resource r: totalOutput.keySet()) strongBox.addResource(r, totalOutput.get(r));
    }

    /**
     * General parametric method that finds, in a <code>Collection</code> of <code>Identifiable</code> objects, the element with the desired <code>id</code>.
     *
     * @param id                            The <code>id</code> of the element to search.
     * @param collection                    The <Code>Collection</Code> where to search.
     * @param <E>                           The type of the object which is forming the <code>Collection</code>.
     * @return                              The first of the objects with the specified <code>id</code>.
     * @throws ElementNotFoundException     If no object in the <code>Collection</code> has the specified <code>id</code>.
     */
    public <E extends Identifiable> E findById(int id, Collection<E> collection) throws ElementNotFoundException{
        E result = collection.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(null);
        if (result == null)
            throw new ElementNotFoundException();
        return result;
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

        if(chosenResources.getAgnosticInput() != 0 || chosenResources.getAgnosticOutput() != 0)
            return false;
        var chosenInput = chosenResources.getInputResources();
        var chosenOutput = chosenResources.getOutputResources();

        if(chosenInput.containsKey(Resource.FAITH) || chosenOutput.containsKey(Resource.FAITH))
            return false;

        if(power.getAgnosticInput() != chosenInput.values().stream().mapToInt(Integer::intValue).sum())
            return false;

        if(power.getAgnosticOutput() != chosenInput.values().stream().mapToInt(Integer::intValue).sum())
            return false;

        return true;
    }

    /**
     * Adds a new possibly agnostic <code>ProductionPower</code> to the <code>extraProductionPower</code>
     *
     * @param productionPower           The power to add
     */
    public void addExtraProductionPower(ProductionPower productionPower) {

        extraProductionPowers.add(productionPower);
    }

}

