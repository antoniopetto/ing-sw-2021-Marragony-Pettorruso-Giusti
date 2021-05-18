package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;


import java.io.IOException;

public class PutResourceMsg implements CommandMsg {
    private final Marble marble;
    private final DepotName depot;
    private final Resource resource;

    public PutResourceMsg(Marble marble, DepotName depot){
        this(marble, depot, null);
    }

    public PutResourceMsg(Marble marble, DepotName depot, Resource resource) {
        if (depot == null || marble == Marble.WHITE && resource == null || marble != Marble.WHITE && resource != null)
            throw new IllegalArgumentException();
        this.marble = marble;
        this.depot = depot;
        this.resource = resource;
    }

    @Override
    public void execute(Game game){

        if (marble == Marble.WHITE)
            game.putResource(marble, depot, resource);
        else
            game.putResource(marble, depot);
    }

    @Override
    public String toString() {
        return "PutResourceMsg{" +
                "marble=" + marble +
                ", depot=" + depot +
                ", resource=" + resource +
                '}';
    }
}
