package it.polimi.ingsw.model.playerboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class WareHouse {
    private List<Depot> depotList;
    private DepotName depotName;
    private boolean isFirstExtraDepot;

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

    public DepotName getDepotName() { return depotName; }

    public void createExtraDepot(Resource r){
        DepotName dExtraName;
        if(isFirstExtraDepot) dExtraName =DepotName.FIRST_EXTRA;
            else dExtraName = DepotName.SECOND_EXTRA;
            this.depotList.add( new Depot(dExtraName, dExtraName.getCapacity(), r));
    }

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

    public void insert(Depot depot, Resource r, int quantity){ for(int i = 0; i < quantity; i++) depot.addResource(r); }

    public void switchDepots(DepotName depotName1, DepotName depotName2){

    }

    private DepotName setDepot(DepotName depotName){
        return this.depotName = depotName;
    }

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

    public void takeResource( Resource r){ this.depotList.get(getDepotName().getPosition()).removeResource(); }

    public int countTotalResources(){
        int totalResources = 0;
        for( Depot depot : this.depotList) totalResources += depot.getQuantity();
        return totalResources;
    }
}
