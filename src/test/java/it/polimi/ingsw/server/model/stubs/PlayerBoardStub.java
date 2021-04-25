package it.polimi.ingsw.server.model.stubs;

import it.polimi.ingsw.server.model.cards.ResourceRequirement;
import it.polimi.ingsw.server.model.playerboard.PlayerBoard;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.playerboard.Slot;

import java.util.List;
import java.util.Map;

public class PlayerBoardStub extends PlayerBoard {
    private Map<Resource, Integer> resources;
    private List<Slot> slots;

    public PlayerBoardStub(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public PlayerBoardStub(List<Slot> slotList)
    {
        slots=slotList;
    }

    @Override
    public boolean isAffordable(ResourceRequirement resourceRequirement) {
        if(resources.containsKey(resourceRequirement.getResource()))
        {
            int quantity = resources.get(resourceRequirement.getResource());
            return quantity>=resourceRequirement.getQuantity();
        }
        return false;

    }

    @Override
    public List<Slot> getSlotList() {
        return slots;
    }
}
