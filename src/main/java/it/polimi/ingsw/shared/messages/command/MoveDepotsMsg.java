package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.shared.DepotName;


/**
 * it is called when We move resources from a normal depot to an extra depot or vice versa
 * A message that sends two depots,
 * depotFrom: depot from which to take resources
 * depotTo : depot to fill with contained Resources in depotFrom
 *
 * @see CommandMsg
 */
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
