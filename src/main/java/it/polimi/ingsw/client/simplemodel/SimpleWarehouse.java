package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.shared.DepotName;

import java.io.Serializable;
import java.util.*;

/**
 * Simple version of Warehouse. Contains just its depots
 */
public class SimpleWarehouse implements Serializable {

    private final List<SimpleDepot> depots;

    /**
     * Default constructor, generates three depots of capacity 1, 2 and 3
     */
    public SimpleWarehouse() {
        depots = new ArrayList<>();
        depots.add(new SimpleDepot(DepotName.HIGH, 1, null, null, 0));
        depots.add(new SimpleDepot(DepotName.MEDIUM, 2, null, null, 0));
        depots.add(new SimpleDepot(DepotName.LOW, 3, null, null, 0));
    }

    public SimpleWarehouse(List<SimpleDepot> depots) {
        this.depots = new ArrayList<>(depots);
    }

    public List<SimpleDepot> getDepots() {
        return depots;
    }

    public SimpleDepot getDepot(DepotName name){
        return depots.stream().filter(i -> i.getName() == name).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return depots.toString();
    }
}
