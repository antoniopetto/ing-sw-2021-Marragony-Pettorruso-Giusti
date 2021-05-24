package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.model.playerboard.DepotName;

public class MoveDepotsMsg implements CommandMsg {

    private final DepotName depotFrom;
    private final DepotName depotTo;

    public MoveDepotsMsg(DepotName depotFrom, DepotName depotTo) {
        this.depotFrom = depotFrom;
        this.depotTo = depotTo;
    }

    @Override
    public void execute(GameController gameController){
        gameController.moveDepots(depotFrom,depotTo);
    }

    @Override
    public String toString() {
        return "MoveDepotsMsg{" +
                "depotFrom=" + depotFrom +
                ", depotTo=" + depotTo +
                '}';
    }
}
