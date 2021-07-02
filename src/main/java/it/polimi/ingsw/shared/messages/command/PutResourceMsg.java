package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.Marble;


/**
 * It is called when a player decides to put a resource corresponding to a marble in a 'depot' Depot
 * When the selected marble is a white Marble, it will be filtered based on its own associated resource and placed in depot
 *
 * @see CommandMsg
 */
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
    public void execute(GameController gameController){

        if (marble == Marble.WHITE)
            gameController.putResource(marble, depot, resource);
        else
            gameController.putResource(marble, depot);
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
