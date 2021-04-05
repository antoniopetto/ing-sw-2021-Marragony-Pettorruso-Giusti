package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ResourceRequirement;

import java.util.List;

public class PlayerBoard {

    private StrongBox strongBox;
    private WareHouse wareHouse;
    private List<Slot> slotList;

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

    public void addInStrongBox(Resource r){ this.strongBox.addResource(r);}

    public boolean compareWithStrongBox(Resource r, int quantity){
        if(this.strongBox.getQuantity(r)>= quantity)return true;
        else return false;
    }

    public boolean addCard(DevelopmentCard developmentCardcard, int idSlot){ // o card in generale?
        if(this.slotList.get(idSlot).addCard(developmentCardcard)) return true;
            else return false;
    }

    public boolean isAffordable(ResourceRequirement resourceRequirement){


        //if(! compareWithStrongBox(resourceRequirement.getResource(),resourceRequirement.getQuantity()) &&
        //      !this.wareHouse.totalResourcesofAType(resourceRequirement.getResource())>=resourceRequirement.getQuantity())
        //      return false;
        //else
        return true;
    }

    public void pay(ResourceRequirement resourceRequirement){
        boolean resourceInStrongBox = true;
        Resource r = Resource.COIN;
        for(int i = 0; i < 1 ; i++) { //al posto di 1 va resourceRequirement.getQuantity();
            if(resourceInStrongBox) resourceInStrongBox = this.strongBox.removeResource(r/*resourceRequirement.getResource()*/);
                else this.wareHouse.takeResource(r/*resourceRequirement.getResource()*/);
        }
    }

    public int countVictoryPoints(){
        int sumCardPoints = 0;

        for(Slot slot : this.slotList ) sumCardPoints+= slot.countCardPoints();

        return this.strongBox.countTotalResources()
                + this.wareHouse.countTotalResources() + sumCardPoints;
    }

}
