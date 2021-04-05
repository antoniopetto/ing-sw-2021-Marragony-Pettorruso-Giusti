package it.polimi.ingsw.model;

import it.polimi.ingsw.model.playerboard.PlayerBoard;
import it.polimi.ingsw.model.playerboard.Resource;

import java.util.HashSet;
import java.util.Set;

public class Player {
    private PlayerBoard playerBoard;
    private Set<Resource> activeDiscount = new HashSet<Resource>();
    private Set<Resource> whiteMarbleAliases = new HashSet<Resource>();
    private Set<Resource> extraProductionPower = new HashSet<Resource>();

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }

    public Set<Resource> getActiveDiscount() {
        return activeDiscount;
    }

    public void setActiveDiscount(Resource resource) {
        this.activeDiscount.add(resource);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    public void setWhiteMarbleAliases(Resource resource) {
        this.whiteMarbleAliases.add(resource);
    }

    public void setExtraProductionPower(Resource res) {
        this.extraProductionPower.add(res);
    }
}
