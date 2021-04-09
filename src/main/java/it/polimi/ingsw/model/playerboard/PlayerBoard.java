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

    private final StrongBox strongBox = new StrongBox();
    private final WareHouse wareHouse = new WareHouse();
    private final List<Slot> slotList = new ArrayList<>();
    private final List<ProductionPower> extraProductionPowers = new ArrayList<>();

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     */
    public PlayerBoard() {

        this.slotList.add( new Slot(1));
        this.slotList.add( new Slot(2));
        this.slotList.add( new Slot(3));
        addExtraProductionPower(new ProductionPower(2, 1));
    }

    public WareHouse getWareHouse() { return wareHouse; }

    public List<Slot> getSlotList() { return slotList; }

    public StrongBox getStrongBox() { return strongBox; }

    /**
     * Adds Resource r in the StrongBox
     *
     * @param r the Resource to add in the StrongBox
     */
    public void addInStrongBox(Resource r){ this.strongBox.addResource(r);}

    /**
     *
     * @param r is the resource that the player wants to compare
     * @param quantity is the total of resources r
     * @return true if there are more resources in the StrongBox than the amount of <code>Resources</code> r
     */
    public boolean compareWithStrongBox(Resource r, int quantity){
        if(this.strongBox.getQuantity(r)>= quantity)return true;
        else return false;
    }

    /**
     *
     * @param developmentCard is the <code>DevelopmentCard</code> the player tries to add
     * @param idSlot represents the number of the <code>Slot</code> in which to insert the <code>DevelopmentCard</code>
     * @return true if it passes all checks in the 'idSlot' <code>Slot</code> and then add the developmentCard in the slot
     */
    public boolean addCard(DevelopmentCard developmentCard, int idSlot){ // o card in generale?
        if(this.slotList.get(idSlot).addCard(developmentCard)) return true;
            else return false;
    }

    /**
     *
     * @param resourceRequirement contains the <code>Resources</code> to be controlled in the <code>WareHouse</code> / <code>StrongBox</code>
     * @return true if the amount of required resources is present in the <code>Depot</code> or <code>WareHouse</code>
     */
    public boolean isAffordable(ResourceRequirement resourceRequirement){

        if(! compareWithStrongBox(resourceRequirement.getResource(), resourceRequirement.getQuantity())&&
                ! ( this.wareHouse.totalResourcesofAType(resourceRequirement.getResource())>=resourceRequirement.getQuantity() ) )
                  return false;
        else return true;
    }

    /**
     * Removes first from the strongBox and then if in the strongBox there aren't enough Resources of the required type , removes from wareHouse
     *
     * @param resourceRequirement represents the <code>Resources</code> to be removed / paid from the strongbox or warehouse
     * to buy a <code>DevelopmentCard</code> or activate production power
     */
    public void pay(ResourceRequirement resourceRequirement){

        boolean resourceInStrongBox = true;

        for(int i = 0; i < resourceRequirement.getQuantity() ; i++) {
            if(resourceInStrongBox) resourceInStrongBox = this.strongBox.removeResource(resourceRequirement.getResource());
                else this.wareHouse.takeResource(resourceRequirement.getResource());
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

    private <E extends Identifiable> E findById(int id, Collection<E> collection) throws ElementNotFoundException{
        E result = collection.stream()
                              .filter(x -> x.getId() == id)
                               .findFirst().orElse(null);
        if (result == null)
            throw new ElementNotFoundException();
        return result;
    }

    private List<DevelopmentCard> getLastDevCards(){
        //TODO implemented as if the slots are implemented via stacks, as we were saying
        List<DevelopmentCard> lastDevCards = new ArrayList<>();
        for (Slot s : slotList){
            try{
                lastDevCards.add(s.getLastCard());
            }
            catch (EmptyStackException e) {}
        }
        return lastDevCards;
    }


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

            var input = power.getInputResources();
            var output = power.getOutputResources();

            sumIOMapsToTotal(totalInput, totalOutput, input, output);
        }

        for(Integer i : selectedExtraPowers.keySet()){

            ProductionPower chosenPower = selectedExtraPowers.get(i);
            var chosenInput = chosenPower.getInputResources();
            var chosenOutput = chosenPower.getOutputResources();

            try {
                power = findById(i, extraProductionPowers);
            }
            catch (ElementNotFoundException e){
                throw new IllegalArgumentException("The user requested a special production he didn't have");
            }

            if (!specialProductionConsistent(power, chosenPower))
                throw new IllegalArgumentException("The client choice of special production is illegal");

            sumIOMapsToTotal(totalInput, totalOutput, chosenInput, chosenOutput);
        }

        for(Resource r : totalInput.keySet())
            if (isAffordable(new ResourceRequirement(r, totalInput.get(r))))
                throw new NotAffordableException();

        for(Resource r : totalInput.keySet())
            pay(new ResourceRequirement(r, totalInput.get(r)));

        //TODO we should replace/add a method to insert resourceRequirements in strongBox instead of Resources
        for(Resource r: totalOutput.keySet())
            for(int i = 0; i < totalOutput.get(r); i++)
                strongBox.addResource(r);

    }

    private void sumIOMapsToTotal(Map<Resource, Integer> totalInput, Map<Resource, Integer> totalOutput,
                                  Map<Resource, Integer> input,      Map<Resource, Integer> output) {

        for(Resource resource : input.keySet())
            totalInput.compute(resource, (k, v) -> (v == null) ? input.get(resource) : v + input.get(resource));

        for(Resource resource : output.keySet())
            totalOutput.compute(resource, (k, v) -> (v == null) ? output.get(resource) : v + output.get(resource));
    }

    //TODO functional solution to check consistency...
    private boolean specialProductionConsistent(ProductionPower power, ProductionPower chosenPower) {

        var input = power.getInputResources();
        var output = power.getOutputResources();
        var chosenInput = chosenPower.getInputResources();
        var chosenOutput = chosenPower.getOutputResources();

        if(chosenInput.containsKey(Resource.FAITH))
            return false;

        var chosenInputStream = chosenInput.entrySet().stream();
        var inputStream = input.entrySet().stream();
        //...here

        return true;
    }

    public void addExtraProductionPower(ProductionPower productionPower) {

        extraProductionPowers.add(productionPower);
    }

}


