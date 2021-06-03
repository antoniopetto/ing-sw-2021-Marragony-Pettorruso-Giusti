package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleWarehouse implements Serializable {

    //TODO wrong type for depot representation (a depot could contain different resources)
    private Map<DepotName, Map<Resource, Integer>> depots = new LinkedHashMap<>();

    public SimpleWarehouse() {
        depots.put(DepotName.HIGH, null);
        depots.put(DepotName.MEDIUM, null);
        depots.put(DepotName.LOW, null);
    }

    public SimpleWarehouse(Map<DepotName, Map<Resource, Integer>> content) {
        this();
        for (DepotName depotName : content.keySet()){
            depots.replace(depotName, content.get(depotName));
        }
    }

    public Map<DepotName, Map<Resource, Integer>> getDepots() {
        return depots;
    }
}
