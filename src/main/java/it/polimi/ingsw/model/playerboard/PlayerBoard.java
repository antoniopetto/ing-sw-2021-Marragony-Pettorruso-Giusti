package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ResourceRequirement;

import java.util.List;

/**
 * This class represents the PlayerBoard of a Player.
 *
 * @see WareHouse
 * @see Slot
 * @see StrongBox
 */
public class PlayerBoard {

    private StrongBox strongBox;
    private WareHouse wareHouse;
    private List<Slot> slotList;

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     * @param strongBox the chest that contains resources
     * @param wareHouse the WareHouse consisting of Depots
     * @param slotList the list of slots
     */
    public PlayerBoard(StrongBox strongBox, WareHouse wareHouse, List<Slot> slotList) {
        this.strongBox = strongBox;
        this.wareHouse = wareHouse;
        this.slotList = slotList;
        this.slotList.add( new Slot(1));
        this.slotList.add( new Slot(2));
        this.slotList.add( new Slot(3));
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

}
