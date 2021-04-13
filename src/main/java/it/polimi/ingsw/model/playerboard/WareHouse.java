package it.polimi.ingsw.model.playerboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
     * @return true if 'DepotName' Depot contains constraint and so it is an ExtraDepot
     */
    private boolean isExtraDepot(DepotName depotName){
        return this.depotList.get(depotName.getPosition()).getConstraint().isPresent();
    }

    /**
     * Switch resources between the two depots (depotName1 and depotName2), includes moving between two ExtraDepots, an Extradepot and a normal depot
     * or two normal Depots
     * @param depotName1 the Depot from which to take the resources to be exchanged
     * @param depotName2 the Depot with which to exchange resources
     */
    public void switchDepots(DepotName depotName1, DepotName depotName2){
            if(isExtraDepot(depotName1) && isExtraDepot(depotName2)) switchExtraDepots(depotName1, depotName2, true);
            else if(isExtraDepot(depotName1) || isExtraDepot(depotName2))  switchExtraDepots(depotName1, depotName2, false);
                    else switchNormalDepot(depotName1,depotName2);
    }

    /**
     * Check for resource constraint and that the depotNameA Depot must not be empty
     *
     * @param depotNameA basically it is the depot from which to remove the resources
     * @param depotNameB it is the depot to fill
     * @param allExtraDepots is true if depotNameA and depotNameB are both ExtraDepot
     * da scrivere la documentazione sulle eccezioni
     */
    private void switchExtraDepots(DepotName depotNameA, DepotName depotNameB, boolean allExtraDepots){

        if( depotByName(depotNameA).isEmpty() ) throw new IllegalStateException("DepotA is just empty");

        Function<DepotName,Boolean> controlResource = (depotName) -> {
            if(depotByName(depotName).getConstraint().isEmpty()) return compareResourceType(depotNameB,depotNameA);
                return compareResourceType(depotNameA,depotNameB);
        };

        if ( (!allExtraDepots && depotByName(depotNameB).isEmpty())  || controlResource.apply(depotNameA)) fillTheOtherDepot(depotNameA, depotNameB);
            else throw new IllegalArgumentException("It isn't possible switch the Resources between Depots, " +
                                                "the type of resource that the player tries to insert in the extraDepot is different from the constraint ");

    }

    /**
     *
     * @param depotNameA is the name of ExtraDepot
     * @param depotNameB is the name of Normal Depot
     * @return true if the constraint of the depotNameA Depot is equal to the resource contained in the depotNameB Depot
     */
    private boolean compareResourceType(DepotName depotNameA, DepotName depotNameB){
        return depotByName(depotNameA).getConstraint().get().equals(depotByName(depotNameB).getResource());
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
            depotByName(depotToEmpty).removeResourceFromDepot();
        }
    }

    /**
     * Switch resources between the two depots (depotName1 and depotName2 Depots) if there is enough space in both to make the switch
     *
     * @exception IllegalArgumentException if one of the two Depots has the capacity that is less than the amount of Resources in the other Depot
     */
    private void switchNormalDepot(DepotName depotName1, DepotName depotName2){

        if( compareTwoDepots(depotName1, depotName2) && compareTwoDepots(depotName2,depotName1) )
        {
            var resourceTmp = depotByName(depotName2).getResource();
            var quantityTmp = depotByName(depotName2).getQuantity();

            depotByName(depotName2).setResource(depotByName(depotName1).getResource());
            depotByName(depotName2).setQuantity(depotByName(depotName1).getQuantity());

            depotByName(depotName1).setResource(resourceTmp);
            depotByName(depotName1).setQuantity(quantityTmp);
        }
        else throw new IllegalArgumentException("It isn't possible switch the Resources between Depots");

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
