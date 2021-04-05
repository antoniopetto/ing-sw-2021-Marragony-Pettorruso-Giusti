package it.polimi.ingsw.model.playerboard;

import java.util.Optional;

public class Depot {

    private Resource resource;
    private  DepotName name;
    private int quantity;
    private int capacity;
   // private Optional<Resource> constraint;
    private Resource resourceExtra;

    public Depot(DepotName name, int capacity, Resource resourceExtra) {
        this.name = name;
        this.quantity = 0;
        this.capacity = capacity;
        this.resourceExtra = resourceExtra;
    }

    public Depot(DepotName name, int capacity){
        this.name = name;
        this.quantity = 0;
        this.capacity = capacity;
    }

    public DepotName getName() { return name; }

    public int getCapacity() {
        return capacity;
    }

    public Resource getResource(){
        if(this.isEmpty()) throw new IllegalArgumentException("The" + this.name +
                                                                " DEPOT is empty!");
            return resource; }

    public int getQuantity(){ return quantity; }

    public void setResource(Resource resource) { this.resource = resource; }

    public void addResource(Resource r){
            if(this.isEmpty()) this.resource = r;
            this.quantity++;
    }

    public void removeResource(){
        this.quantity--;
        if(this.isEmpty()) this.resource = null;
    }

    public boolean isEmpty(){ return this.getQuantity() == 0; }

    public int spaceAvailable(){
        return this.getCapacity()-this.getQuantity();
    }
}
