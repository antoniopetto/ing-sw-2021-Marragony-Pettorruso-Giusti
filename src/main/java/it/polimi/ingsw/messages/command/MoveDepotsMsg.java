package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;

import java.io.IOException;

public class MoveDepotsMsg implements CommandMsg {

    private final DepotName depotFrom;
    private final DepotName depotTo;

    public MoveDepotsMsg(DepotName depotFrom, DepotName depotTo) {
        this.depotFrom = depotFrom;
        this.depotTo = depotTo;
    }

    @Override
    public void execute(Game game){
        game.moveDepots(depotFrom,depotTo);
    }

    @Override
    public String toString() {
        return "MoveDepotsMsg{" +
                "depotFrom=" + depotFrom +
                ", depotTo=" + depotTo +
                '}';
    }
}
