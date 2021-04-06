package it.polimi.ingsw.model.playerboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the WareHouse
 * It will contain 3 Depots and a maximum of two ExtraDepots
 *
 * @see Depot
 * @see Resource
 */
public class WareHouse {
    private List<Depot> depotList;
    private DepotName depotName;
    private boolean isFirstExtraDepot;

    /**
     * Constructs the WareHouse
     * Instances 3 Depots
     */
    public WareHouse() {

        this.depotList = new ArrayList<>();
        this.depotList.add(new Depot(DepotName.HIGH,DepotName.HIGH.getCapacity()));
        this.depotList.add(new Depot(DepotName.MEDIUM,DepotName.MEDIUM.getCapacity()));
        this.depotList.add(new Depot(DepotName.LOW,DepotName.LOW.getCapacity()));
        this.isFirstExtraDepot = true;
    }

    public List<Depot> getDepots() {
        return this.depotList;
    }

    /**
     * Creates a ExtraDepot
     *
     * @param r   It is the <code>Resource</code> constraint of the ExtraDepot
     */
    public void createExtraDepot(Resource r){
        DepotName dExtraName;
        if(isFirstExtraDepot) dExtraName =DepotName.FIRST_EXTRA;
            else dExtraName = DepotName.SECOND_EXTRA;

            this.depotList.add( new Depot(dExtraName, dExtraName.getCapacity(), r));
    }

    /**
     * Checks if there aren't other Depots that contain the <code>Resource</code> r and then
     * if the 'depotName' Depot has enough space, the <code>Resource</code> r will be placed in the 'depotName' Depot
     *
     * @param depotName indicates in which Depot to add the <code>Resource</code> r
     * @param r is the <code>Resource</code> that the player wants to insert in the WareHouse
     * @param quantity is the number of resources to insert
     * @return true if there is an empty space of the select Depot in which to insert the <code>Resource</code>
     */
    public boolean isInsertable(DepotName depotName, Resource r, int quantity){
        Depot depotToInsert = this.depotList.get(depotName.getPosition());

        List<Depot> depotList1 = this.depotList.stream()
                                                    .filter(d -> !d.getName().equals(depotName))
                                                        .collect(Collectors.toList());

        for(Depot depot : depotList1) if(depot.getResource().equals(r)) return false;

        if( (depotToInsert.isEmpty() && quantity <= depotToInsert.getCapacity()) ||
                (depotToInsert.getResource().equals(r) && quantity <= depotToInsert.spaceAvailable()))
        {
            this.insert(depotToInsert,r,quantity);
            return true;
        }
        else return false;

    }

    /**
     * Inserts the number of <code>Resources</code> r in the 'depot' Depot
     *
     */
    public void insert(Depot depot, Resource r, int quantity){ for(int i = 0; i < quantity; i++) depot.addResource(r); }

    /**
     * Exchanges resources between the two depots (depotName1 and depotName2)
     *
     * @param depotName1 the Depot from which to take the resources to be exchanged
     * @param depotName2 the Depot with which to exchange resources
     * @exception IllegalArgumentException if It isn't possible switch the Resources between Depots
     */
    public void switchDepots(DepotName depotName1, DepotName depotName2){
        if(this.depotList.get(depotName1.getPosition()).getQuantity() <= this.depotList.get(depotName2.getPosition()).getCapacity() &&
                this.depotList.get(depotName2.getPosition()).getQuantity() <= this.depotList.get(depotName1.getPosition()).getCapacity() &&
                (!this.depotList.get(depotName1.getPosition()).getConstraint().isPresent() ||
                        this.depotList.get(depotName1.getPosition()).getConstraint().equals(this.depotList.get(depotName2.getPosition()).getResource()))
            && (!this.depotList.get(depotName2.getPosition()).getConstraint().isPresent() ||
                this.depotList.get(depotName2.getPosition()).getConstraint().equals(this.depotList.get(depotName1.getPosition()).getResource())) )
        {
            Resource resourceTmp = this.depotList.get(depotName2.getPosition()).getResource();
            int quantityTmp = this.depotList.get(depotName2.getPosition()).getQuantity();

            this.depotList.get(depotName2.getPosition())
                                            .setResource(this.depotList.get(depotName1.getPosition()).getResource());

            this.depotList.get(depotName2.getPosition())
                                            .setQuantity(this.depotList.get(depotName1.getPosition()).getQuantity());

            this.depotList.get(depotName1.getPosition()).setResource(resourceTmp);
            this.depotList.get(depotName1.getPosition()).setQuantity(quantityTmp);

        }
        else throw new IllegalArgumentException("It isn't possible switch the Resources between Depots");

    }

    private DepotName setDepot(DepotName depotName){
        return this.depotName = depotName;
    }

    /**
     *
     * @param r the Resource to count
     * @return the total number of Resources r
     */
    public int totalResourcesofAType( Resource r){
        int totalResources = 0;
        for( Depot depot : this.depotList)
        {
            if(depot.getResource().equals(r)) {
                if (!depot.isEmpty()) {
                    setDepot(depot.getName());
                    totalResources += depot.getQuantity();
                }
            }
        }
        return totalResources;
    }

    public DepotName getDepotName() { return depotName; }

    /**
     * Removes the <code>Resource</code> r  from a Depot that contains it
     * @param r the Resource to remove
     */
    public void takeResource( Resource r){ this.depotList.get(getDepotName().getPosition()).removeResource(); }

    /**
     *
     * @return the total number of resources in WareHouse
     */
    public int countTotalResources(){
        int totalResources = 0;
        for( Depot depot : this.depotList) totalResources += depot.getQuantity();
        return totalResources;
    }
}
