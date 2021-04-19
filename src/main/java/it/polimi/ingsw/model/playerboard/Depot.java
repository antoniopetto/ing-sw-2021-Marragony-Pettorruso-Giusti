package it.polimi.ingsw.model.playerboard;
import java.util.Optional;

/**
 *This class represents the Depot.
 * It will contain a number between 0 and 3 resources
 *
 * @see DepotName
 * @see Resource
 */
public class Depot {

    /**
     * It indicates the number of resources present in this depot
     */
    private int quantity;

    private final DepotName name;
    private final int capacity;
    private final Optional<Resource> constraint;
    private Resource resource;

    /**
     * Constructs the Depot
     * @param name is the name of this Depot
     * @param capacity indicates the maximum number of resources that can be inserted in this Depot
     * @param constraint indicates the only resource that can be inserted if the Depot is a <code>ExtraDepot</code>
     */
    public Depot(DepotName name, int capacity, Resource constraint) {
        this.name = name;
        this.quantity = 0;
        this.capacity = capacity;
        this.constraint = Optional.of(constraint);
        this.resource = constraint;

    }

    public Depot(DepotName name, int capacity){
        this.name = name;
        this.quantity = 0;
        this.capacity = capacity;
        this.constraint = Optional.empty();
        this.resource = null;
    }

    public void setQuantity(int quantity) {
        if(quantity>getCapacity()) throw new IllegalArgumentException("Error");
        this.quantity = quantity;
    }

    public void setResource(Resource resource) {
        if(constraint.isEmpty() || constraint.get().equals(resource)) this.resource = resource;
            else throw new IllegalArgumentException("There is a constraint");
    }

    public DepotName getName() { return name; }

    public Optional<Resource> getConstraint() { return constraint; }

    public int getCapacity() {
        return capacity;
    }

    /**
     *
     * @return the type of <code>Resource</code> inserted in the Depot
     */
    public Resource getResource(){
        //if(this.isEmpty()) throw new IllegalArgumentException("It is no resource");
                return this.resource;
    }

    public int getQuantity(){ return quantity; }

    /**
     * Adds the <code>Resource</code> r in the Depot
     *
     * @param r is the <code>Resource</code> that the player wants to insert in the Depot
     * @exception IllegalArgumentException if the Depot is an ExtraDepot and the Resource r type is not equal to constraint
     */
    public void addResource(Resource r) {

        if( constraint.isEmpty()  || constraint.get().equals(r)){
            if(this.isFull()) throw new IllegalArgumentException("The Depot is full!");

            if(this.isEmpty()){
                this.resource = r;
                this.quantity++;
            }
            else if(this.getResource().equals(r)) this.quantity++;
                else throw new IllegalArgumentException("The type of resource that the player wants to insert does not match the type of resource already present");
        }
        else throw new IllegalArgumentException("The inserted Resource r doesn't match the special resource");
    }

    /**
     * Removes an unit of Resource from the Depot if this Depot is not Empty
     *
     * @exception IllegalArgumentException when there is no Resource in this Depot
     */
    public void removeResourceFromDepot(){

        if(!this.isEmpty()) this.quantity--;
            else throw new IllegalArgumentException("There is no Resource  in this Depot");

    }

    /**
     *
     * @return true if the Depot contains no <code>Resources</code>
     */
    public boolean isEmpty(){ return this.getQuantity() == 0; }

    /**
     *
     * @return true if the Depot has no space available
     */
    public boolean isFull(){
        return this.getCapacity()==this.getQuantity();
    }


}
