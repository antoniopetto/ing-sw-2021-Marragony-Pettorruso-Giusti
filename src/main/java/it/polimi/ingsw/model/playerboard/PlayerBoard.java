package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ResourceRequirement;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     *
     */
    public PlayerBoard() {
        this.strongBox = new StrongBox();
        this.wareHouse = new WareHouse();
        this.slotList = new ArrayList<>();
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
     * @param developmentCard is the <code>DevelopmentCard</code> the player tries to add
     * @param idSlot represents the number of the <code>Slot</code> in which to insert the <code>DevelopmentCard</code>
     * @return true if it passes all checks in the 'idSlot' <code>Slot</code> and then add the developmentCard in the slot
     */
    public void addCard(DevelopmentCard developmentCard, int idSlot){
        if(slotList.get(idSlot).tryAddCard(developmentCard)) slotList.get(idSlot).addCard(developmentCard);
            else throw new IllegalArgumentException("Unable to insert development card in this slot, change destination slot");
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

        boolean resourceInWareHouse = true;
        int i = 0;

        while( i < resourceRequirement.getQuantity() ) {

            if(resourceInWareHouse) {
                try {
                    wareHouse.resourceResourcefromWareHouse(resourceRequirement.getResource());
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

}
