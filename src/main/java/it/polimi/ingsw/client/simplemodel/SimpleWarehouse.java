package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SimpleWarehouse implements Serializable {
    private Map<DepotName, Map<Resource, Integer>> depots;

    public SimpleWarehouse()
    {
        depots=new HashMap<>();
        depots.put(DepotName.HIGH, null);
        depots.put(DepotName.MEDIUM, null);
        depots.put(DepotName.LOW, null);
    }

    public SimpleWarehouse(Map<DepotName, Map<Resource, Integer>> depots) {
        this.depots = depots;
    }

    public void setWarehouse(Map<DepotName, Map<Resource, Integer>> newState)
    {
        depots = newState;
    }

    public Map<DepotName, Map<Resource, Integer>> getDepots() {
        return depots;
    }
}
