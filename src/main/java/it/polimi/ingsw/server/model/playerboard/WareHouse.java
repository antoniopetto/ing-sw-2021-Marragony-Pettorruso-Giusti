package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.client.simplemodel.SimpleDepot;
import it.polimi.ingsw.client.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * This class represents the WareHouse
 * It will contain 3 Depots and a maximum of two ExtraDepots
 *
 * @see Depot
 * @see Resource
 */
public class WareHouse implements Serializable {
    private final List<Depot> depotList;
    private transient VirtualView virtualView;

    /**
     * Constructs the WareHouse
     * Instances 3 Depots
     */
    public WareHouse() {
        this.depotList = new ArrayList<>();
        this.depotList.add(new Depot(DepotName.HIGH,1));
        this.depotList.add(new Depot(DepotName.MEDIUM,2));
        this.depotList.add(new Depot(DepotName.LOW,3));
    }

    public void setVirtualView(VirtualView virtualView){
        this.virtualView = virtualView;
    }

    public SimpleWarehouse getSimple(){

        List<SimpleDepot> content = new ArrayList<>();
        for (Depot depot : depotList)
            content.add(depot.getSimple());

        return new SimpleWarehouse(content);
    }

    public List<Depot> getDepots() {
        return this.depotList;
    }

    /**
     * Creates a ExtraDepot
     *
     * @param constraint   It is the <code>Resource</code> constraint of the ExtraDepot
     * @param capacity     The capacity of the ExtraDepot
     */
    public void createExtraDepot(Resource constraint, int capacity){

        if(depotList.size() == 3)
            depotList.add(new Depot(DepotName.FIRST_EXTRA, capacity, constraint));
        else if(depotList.size() == 4)
            depotList.add(new Depot(DepotName.SECOND_EXTRA, capacity, constraint));
        else throw new IllegalArgumentException("Trying to add too many extraDepots");

        virtualView.warehouseUpdate();
    }

    /**
     * Checks if there aren't other Depots that contain the <code>Resource</code> r
     *
     * @param depotName indicates in which Depot to add the <code>Resource</code> r
     * @param r is the <code>Resource</code> that the player wants to insert in the WareHouse
     * @return true if there is an empty space of the select Depot in which to insert the <code>Resource</code>
     */
    public boolean isInsertable(DepotName depotName, Resource r){

        Depot depotToInsert = depotByName(depotName);

        if(depotToInsert.getConstraint() == null) {

            List<Depot> depotList1 = this.depotList.stream()
                    .filter(d -> !d.getName().equals(depotName))
                    .filter(d -> d.getConstraint() == null)
                    .filter(d -> !d.isEmpty())
                    .collect(Collectors.toList());

            for (Depot depot : depotList1) if (depot.getResource().equals(r)) return false;

            return depotToInsert.isEmpty() || (depotToInsert.getResource().equals(r) && !depotToInsert.isFull());

        } else return depotToInsert.getConstraint().equals(r) && !depotToInsert.isFull();

    }

    /**
     * If the 'depotName' Depot has enough space, inserts the number of <code>Resources</code> r in the 'depotName' Depot
     * @param depotName the Name of Depot in which to place the resource
     * @param r the Resource to add
     */
    public void insert(DepotName depotName, Resource r){
        if(isInsertable(depotName,r)) {
            depotByName(depotName).addResource(r);
            virtualView.warehouseUpdate();
        }
          else throw new IllegalArgumentException("There isn't an empty space of the select Depot in which to insert the Resource r" +
                                                    "or The Resource that the player wants to insert does not match " +
                                                    "the one already inserted in that Depot");
    }


    /**
     * depotName1 and depotName2 are the names of the Depots to compare
     *
     * @return true if the total number of resources in the 'depotName1' Depot is less than / equal to the total capacity of the 'depotName2' Depot
     */
    private boolean compareTwoDepots(DepotName depotName1, DepotName depotName2){
        return  this.depotList.get(depotName1.getPosition()).getQuantity() <= this.depotList.get(depotName2.getPosition()).getCapacity();
    }

    /**
     *
     * @param depotName the DepotName of the Depot to analyze
     * @return true if 'DepotName' Depot contains constraint and so it is an ExtraDepot
     */
    private boolean isExtraDepot(DepotName depotName){
        return this.depotList.get(depotName.getPosition()).getConstraint() != null;
    }

    /**
     * Switch resources between the two depots (depotName1 and depotName2), includes moving between two ExtraDepots, an Extradepot and a normal depot
     * or two normal Depots
     * @param depotName1 the Depot from which to take the resources to be exchanged
     * @param depotName2 the Depot with which to exchange resources
     */
    public void switchDepots(DepotName depotName1, DepotName depotName2) throws Exception {
            if(isExtraDepot(depotName1) || isExtraDepot(depotName2))  virtualView.sendError("It isn't possible to exchange resources from a Depot to an extraDepot or in reverse");
                    else {
                if(!(depotByName(depotName1).isEmpty() && depotByName(depotName2).isEmpty())){
                    if( compareTwoDepots(depotName1, depotName2) && compareTwoDepots(depotName2,depotName1))
                    {
                        //Utilizzo di una risorsa Empty? Meglio del null o no?
                        Resource resourceTmp1;
                        Resource resourceTmp2;
                        int quantityTmp1;
                        int quantityTmp2;

                        if(depotByName(depotName2).isEmpty()){
                            resourceTmp2 = null;
                            quantityTmp2 = 0;
                        }else {
                            resourceTmp2 = depotByName(depotName2).getResource();
                            quantityTmp2 = depotByName(depotName2).getQuantity();
                        }

                        if(depotByName(depotName1).isEmpty()){
                            resourceTmp1 = null;
                            quantityTmp1 = 0;
                        }else {
                            resourceTmp1 = depotByName(depotName1).getResource();
                            quantityTmp1 = depotByName(depotName1).getQuantity();
                        }

                        depotByName(depotName2).setResource(resourceTmp1);
                        depotByName(depotName2).setQuantity(quantityTmp1);

                        depotByName(depotName1).setResource(resourceTmp2);
                        depotByName(depotName1).setQuantity(quantityTmp2);

                        virtualView.warehouseUpdate();
                    }
                    else throw new Exception("It isn't possible switch the Resources between Depots");
                }
                else virtualView.sendError("Both depots are empty!");

            }

    }

    /**
     * Check for resource constraint and that the depotNameA Depot must not be empty
     *
     * @param depotToEmpty basically it is the depot from which to remove the resources
     * @param depotToFill it is the depot to fill
     * da scrivere la documentazione sulle eccezioni
     */
    public void moveDepots(DepotName depotToEmpty, DepotName depotToFill){

        if( depotByName(depotToEmpty).isEmpty() ) throw new IllegalStateException("Depot to remove resources from is already empty");

        Function<DepotName,Boolean> controlResource = (depotName) -> {
            if(depotByName(depotName).getConstraint() == null) return compareResourceType(depotToFill,depotToEmpty);
                return compareResourceType(depotToEmpty,depotToFill);
        };

        if ( !depotByName(depotToFill).isEmpty() || controlResource.apply(depotToEmpty)){
            fillTheOtherDepot(depotToEmpty, depotToFill);
            virtualView.warehouseUpdate();
        }
        else throw new IllegalArgumentException("It isn't possible switch the Resources between Depots, " +
                                                "the type of resource that the player tries to insert in the extraDepot is different from the constraint ");

    }

    /**
     *
     * @param depotToEmpty is the name of ExtraDepot
     * @param depotToFill is the name of Normal Depot
     * @return true if the constraint of the depotToEmpty Depot is equal to the resource contained in the depotToFill Depot
     */
    private boolean compareResourceType(DepotName depotToEmpty, DepotName depotToFill){
        return depotByName(depotToEmpty).getConstraint().equals(depotByName(depotToFill).getResource());
    }

    /**
     * Moves resources from depotToEmpty Depot to depotToFill Depot until the depotToFill Depot is full or the depotToEmpty is empty
     *
     * @param depotToEmpty the name of the Depot from which to fetch resources
     * @param depotToFill the name of the Depot to fill
     */
    private void fillTheOtherDepot(DepotName depotToEmpty, DepotName depotToFill){

        while(!depotByName(depotToFill).isFull() || !depotByName(depotToEmpty).isEmpty()){
            depotByName(depotToFill).addResource(depotByName(depotToEmpty).getResource());
            depotByName(depotToEmpty).removeResource();
        }
    }



    /**
     *
     * @param depotName the name of Depot
     * @return the Depot in the depotList found by its name
     */
    public Depot depotByName( DepotName depotName){ return depotList.get(depotName.getPosition());}

    /**
     *
     * @param r the Resource on which to filter the depots
     * @return a List of Depots which includes the <code>Depots</code> containing the <code>Resource</code> r
     */
    private List<Depot> depotFilter(Resource r){

        return  depotList.stream()
                    .filter(d -> !d.isEmpty())
                        .filter(d -> d.getResource().equals(r))
                            .collect(Collectors.toList());
    }

    /**
     *
     * @param r the Resource to count
     * @return the total number of Resources r in all the Depots
     */
    public int totalResourcesOfAType(Resource r){ return sumOfResInDepotList(this.depotFilter(r)); }

    /**
     * Removes the <code>Resource</code> r  from a Depot that contains it
     * @param r the Resource to remove
     */
    public void removeResource(Resource r){
        Depot depot = depotFilter(r).stream().findFirst().orElse(null);

        if (depot == null)
            throw new IllegalArgumentException("There is no Resource r in WareHouse ");

        depot.removeResource();
        virtualView.warehouseUpdate();
    }

    /**
     *
     * @return the total number of resources in WareHouse
     */
    public int countTotalResources(){ return sumOfResInDepotList(this.depotList); }

    /**
     *
     * @param depotList the set of Depots on which to calculate the sum
     * @return the sum of the resources within the various Depots of the depotList
     */
    private int sumOfResInDepotList(List<Depot> depotList){
        return depotList.stream().map(Depot::getQuantity).reduce(0, Integer::sum);
    }
}
