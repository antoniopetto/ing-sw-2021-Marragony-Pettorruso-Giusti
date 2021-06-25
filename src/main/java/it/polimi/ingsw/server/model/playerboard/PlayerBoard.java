package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.shared.FaithTrack;

import java.io.Serializable;
import java.util.*;
/**
 * This class represents the PlayerBoard of a Player.
 *
 * @see WareHouse
 * @see Slot
 * @see StrongBox
 */
public class PlayerBoard implements Serializable {

    private final StrongBox strongBox;
    private final WareHouse wareHouse;
    private final List<Slot> slotList = List.of(new Slot(), new Slot(), new Slot());
    private final List<ProductionPower> extraProductionPowers = new ArrayList<>(List.of(new ProductionPower(2, 1)));
    private transient VirtualView virtualView;

    /**
     * Constructs the PlayerBoard
     * It will contain a StrongBox, a WareHouse, three Slots
     */
    public PlayerBoard() {
        wareHouse = new WareHouse();
        strongBox = new StrongBox();





    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
        wareHouse.setVirtualView(virtualView);
        strongBox.setVirtualView(virtualView);

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

    public void addCardInSlot(DevelopmentCard developmentCard, int slotIdx){
        slotList.get(slotIdx).addCard(developmentCard);
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

        if(!isAffordable(resourceRequirement))
            throw new IllegalArgumentException("The player doesn't have enough resources");

        for (int i = 0; i < resourceRequirement.getQuantity(); i++) {
            try {
                wareHouse.removeResource(resourceRequirement.getResource());
            } catch (IllegalArgumentException e) {
                strongBox.removeResource(resourceRequirement.getResource());
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
     * A support method to quickly retrieve a list containing the first element of the 3 development card slots.
     *
     * @return                         A <code>List</code> with the first element of each of the <code>Slot</code>s
     */
    public List<DevelopmentCard> getLastDevCards(){

        List<DevelopmentCard> lastDevCards = new ArrayList<>();
        for (Slot s : slotList){
            if(!s.isEmpty())
                lastDevCards.add(s.getLastCard());
        }
        return lastDevCards;
    }

    /**
     * Adds a new possibly agnostic <code>ProductionPower</code> to the <code>extraProductionPower</code>
     *
     * @param productionPower           The power to add
     */
    public void addExtraProductionPower(ProductionPower productionPower) {

        extraProductionPowers.add(productionPower);
        virtualView.extraPowerUpdate(productionPower);
    }
}

