package it.polimi.ingsw.model.playerboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * This class represents the WareHouse
 * It will contain 3 Depots and a maximum of two ExtraDepots
 *
 * @see Depot
 * @see Resource
 */
public class WareHouse {
    private final List<Depot> depotList;
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
        if(isFirstExtraDepot){
            dExtraName =DepotName.FIRST_EXTRA;
            isFirstExtraDepot = false;
        }
        else dExtraName = DepotName.SECOND_EXTRA;

        this.depotList.add( new Depot(dExtraName, dExtraName.getCapacity(), r));
    }

    /**
     * Checks if there aren't other Depots that contain the <code>Resource</code> r and then
     * if the 'depotName' Depot has enough space, the <code>Resource</code> r will be placed in the 'depotName' Depot
     *
     * @param depotName indicates in which Depot to add the <code>Resource</code> r
     * @param r is the <code>Resource</code> that the player wants to insert in the WareHouse
     * @return true if there is an empty space of the select Depot in which to insert the <code>Resource</code>
     */
    public boolean isInsertable(DepotName depotName, Resource r){
        Depot depotToInsert = this.depotList.get(depotName.getPosition());

        List<Depot> depotList1 =  this.depotList.stream()
                                                    .filter(d -> !d.getName().equals(depotName) )
                                                            .filter(d -> d.getConstraint().isEmpty())
                                                                    .collect(Collectors.toList());


        for(Depot depot : depotList1) if(depot.getResource().equals(r)) return false;

        if( depotToInsert.isEmpty() || (depotToInsert.getResource().equals(r) && !depotToInsert.isFull()))
        {
            this.insert(depotToInsert,r);
                return true;
        }
        else return false;

    }

    /**
     * Inserts the number of <code>Resources</code> r in the 'depot' Depot
     * @param depot the depot in which to place the resource
     * @param r the Resource to add
     */
    public void insert(Depot depot, Resource r){ depot.addResource(r); }

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
     * @return true if 'DepotName' Depot contains no constraint and so it isn't a ExtraDepot
     */
    private boolean isNotExtraDepot(DepotName depotName){
        return this.depotList.get(depotName.getPosition()).getConstraint().isEmpty();
    }

    /**
     * Exchanges resources between the two depots (depotName1 and depotName2), ExtraDepots are not included
     *
     * @param depotName1 the Depot from which to take the resources to be exchanged
     * @param depotName2 the Depot with which to exchange resources
     * @exception IllegalArgumentException if It isn't possible switch the Resources between Depots
     */
    public void switchDepots(DepotName depotName1, DepotName depotName2){
        if(  compareTwoDepots(depotName1, depotName2) && compareTwoDepots(depotName2,depotName1)
               && isNotExtraDepot(depotName1)  && isNotExtraDepot(depotName2) )
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

    /**
     *
     * @param r the Resource on which to filter the depots
     * @return a List of Depots which includes the <code>Depots</code> containing the <code>Resource</code> r
     */
    private List<Depot> depotFilter(Resource r){
         return this.depotList.stream()
                                    .filter(d -> !d.isEmpty())
                                            .filter(d -> d.getResource().equals(r))
                                                                .collect(Collectors.toList());

    }
    /**
     *
     * @param r the Resource to count
     * @return the total number of Resources r in all the Depots
     */
    public int totalResourcesOfAType( Resource r){ return sumOfResInDepotList(this.depotFilter(r)); }

    /**
     * Removes the <code>Resource</code> r  from a Depot that contains it
     * @param r the Resource to remove
     */
    public void resourceResourcefromWareHouse( Resource r){
        Optional<Depot> optional = this.depotFilter(r)
                                                .stream()
                                                    .findFirst();

        /*
        optional.ifPresentOrElse( d -> {
            DepotName dName = optional
                    .map(Depot::getName)
                    .get();//gestire warning
                },
                () -> {
                throw new IllegalArgumentException("Problema");
                }
                );
        */

        if(optional.isEmpty()) throw new IllegalArgumentException(" There is no Resource r in WareHouse ");

        DepotName dName = optional
                            .map(Depot::getName)
                                .get();//gestire warning

        this.depotList.get(dName.getPosition()).removeResourceFromDepot();

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
        return depotList.stream()
                            .map(Depot::getQuantity)
                                .reduce(0, Integer::sum);
    }
}
