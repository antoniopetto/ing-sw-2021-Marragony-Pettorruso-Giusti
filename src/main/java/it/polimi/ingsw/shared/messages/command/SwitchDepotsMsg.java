package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;



import java.io.IOException;

public class SwitchDepotsMsg implements CommandMsg {

    private DepotName depot1;
    private DepotName depot2;

    public SwitchDepotsMsg(DepotName depot1, DepotName depot2)
    {
        this.depot1=depot1;
        this.depot2=depot2;
    }
    @Override
    public void execute(Game game, ClientHandler handler){

        game.switchDepots(depot1, depot2);
    }
}
