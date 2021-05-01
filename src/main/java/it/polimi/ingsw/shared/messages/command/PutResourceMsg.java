package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.shared.messages.update.BufferUpdateMsg;
import it.polimi.ingsw.shared.messages.view.ErrorMsg;


import java.io.IOException;
import java.util.Optional;

public class PutResourceMsg implements CommandMsg {
    private final Marble marble;
    private final DepotName depotName;
    private final Resource resource;

    public PutResourceMsg(Marble mar, DepotName dep, Resource res)
    {
        if (mar == Marble.WHITE && res == null || mar != Marble.WHITE && res != null)
            throw new IllegalArgumentException();
        marble = mar;
        depotName = dep;
        resource = res;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {

        boolean result = (marble == Marble.WHITE) ? game.putResource(marble, depotName, resource) :
                game.putResource(marble, depotName);

        if (result) handler.writeObject(new BufferUpdateMsg(marble));
    }
}
