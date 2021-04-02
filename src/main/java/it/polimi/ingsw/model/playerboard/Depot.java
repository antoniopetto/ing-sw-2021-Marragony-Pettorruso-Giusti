package it.polimi.ingsw.model.playerboard;

import java.util.Optional;

public class Depot {

    private Resource resource;
    private  DepotName name;
    private int quantity;
    protected int capacity;
    private Optional<Resource> constraint;

    public Depot(DepotName name, int capacity, Optional<Resource> constraint) {
        this.name = name;
        this.quantity = 0;
        this.capacity = capacity;
        this.constraint = constraint;
    }

    public Resource getResource(){ return resource; }

    public int getQuantity(){ return quantity; }

    public void addResource( Resource r){}

    public boolean removeResource(){
        return true;
    }
}
