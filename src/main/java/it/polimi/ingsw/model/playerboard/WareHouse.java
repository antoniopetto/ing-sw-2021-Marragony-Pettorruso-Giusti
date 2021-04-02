package it.polimi.ingsw.model.playerboard;

import java.util.List;
import java.util.Map;

public class WareHouse {
            private List<Depot> depotList;

    public List<Depot> getDepots() {
        return depotList;
    }

    public boolean isInsertable(Map<DepotName,List<Resource>> map){
        return true;
    }

    public void insert(Map<DepotName,List<Resource>> map){ }

    public void switchDepots(Depot d1, Depot d2){}

    public boolean takeResource( Resource r){
        return true;
    }

    public int countTotalResources(){
        return 5;
    }
}
