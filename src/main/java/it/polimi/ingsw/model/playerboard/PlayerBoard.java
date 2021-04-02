package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.Card;
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
    }

    public void addInStrongBox(Resource r){}

    public boolean takeFromStrongBox( Resource r){
        return true;
    }

    public boolean addCard(Card c){
        return true;
    }

    public boolean isAffordable(List<ResourceRequirement> resourceRequirementList){
        return true;
    }

    public void pay(List<ResourceRequirement> resourceRequirementList){}

    public int countVictoryPoints(){
        return 5;
    }

}
