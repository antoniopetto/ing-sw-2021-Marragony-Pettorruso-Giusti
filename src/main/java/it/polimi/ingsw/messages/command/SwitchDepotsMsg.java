package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;

public class SwitchDepotsMsg implements CommandMsg {

    private DepotName depot1;
    private DepotName depot2;

    public SwitchDepotsMsg(DepotName depot1, DepotName depot2)
    {
        this.depot1=depot1;
        this.depot2=depot2;
    }
    @Override
    public void execute(Game game){

        game.switchDepots(depot1, depot2);
    }

    @Override
    public String toString() {
        return "SwitchDepotsMsg{" +
                "depot1=" + depot1 +
                ", depot2=" + depot2 +
                '}';
    }
}
