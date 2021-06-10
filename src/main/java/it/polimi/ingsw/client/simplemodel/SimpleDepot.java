package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;

public class SimpleDepot implements Serializable {
    /**
     * It indicates the number of resources present in this depot
     */
    private final Resource resource;
    private final int quantity;
    private final DepotName name;
    private final int capacity;
    private final Resource constraint;

    /**
     * Constructs the SimpleDepot
     * @param name is the name of this Depot
     * @param capacity indicates the maximum number of resources that can be inserted in this Depot
     * @param constraint indicates the only resource that can be inserted if the Depot is a <code>ExtraDepot</code>
     */
    public SimpleDepot(DepotName name, int capacity, Resource constraint, Resource resource, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.capacity = capacity;
        this.resource = resource;
        this.constraint = constraint;
    }

    public DepotName getName() { return name; }

    public Resource getConstraint() { return constraint; }

    public int getCapacity() {
        return capacity;
    }

    /**
     *
     * @return the type of <code>Resource</code> inserted in the Depot
     */
    public Resource getResource(){
        return this.resource;
    }

    public int getQuantity(){ return quantity; }

    @Override
    public String toString() {
        return "SimpleDepot{" +
                "resource=" + resource +
                ", quantity=" + quantity +
                ", name=" + name +
                ", capacity=" + capacity +
                ", constraint=" + constraint +
                '}';
    }
}
