package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.shared.messages.server.BufferUpdateMsg;
import it.polimi.ingsw.shared.messages.view.ErrorMsg;


import java.io.IOException;
import java.util.Optional;

public class PutResourceMsg implements CommandMsg {
    private Marble marble;
    private DepotName depotName;
    private Optional<Resource> resource;

    public PutResourceMsg(Marble mar, DepotName dep)
    {
        marble=mar;
        depotName=dep;
        resource=Optional.empty();
    }

    public PutResourceMsg(Marble mar, DepotName dep, Resource res)
    {
        marble=mar;
        depotName= dep;
        resource=Optional.of(res);
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        boolean result;
        String text;
        Object msg;
        result = resource.map(value -> game.putResource(marble, depotName, value)).orElseGet(() -> game.putResource(marble, depotName));
        if (result)
            msg= new BufferUpdateMsg(marble);
        else
        {
            text = "Resource not insertable in that depot";
            msg = new ErrorMsg(text);
        }

        handler.writeObject(msg);
    }
}
