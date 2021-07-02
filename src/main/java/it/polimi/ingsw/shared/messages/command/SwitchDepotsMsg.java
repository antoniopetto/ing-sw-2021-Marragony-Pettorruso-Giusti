package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.shared.DepotName;

/**
 * A message that sends two depots between which to swap their resources
 * @see CommandMsg
 */
public class SwitchDepotsMsg implements CommandMsg {

    private DepotName depot1;
    private DepotName depot2;

    public SwitchDepotsMsg(DepotName depot1, DepotName depot2)
    {
        this.depot1=depot1;
        this.depot2=depot2;
    }
    @Override
    public void execute(GameController gameController){

        gameController.switchDepots(depot1, depot2);
    }

    @Override
    public String toString() {
        return "SwitchDepotsMsg{" +
                "depot1=" + depot1 +
                ", depot2=" + depot2 +
                '}';
    }
}
