package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.messages.command.PutResourceMsg;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.shared.Marble;

import java.io.IOException;
import java.util.List;

public class CreateBufferMsg implements UpdateMsg {
    private List<Marble> marbleBuffer;

    public CreateBufferMsg(List<Marble> marbleBuffer) {
        this.marbleBuffer = marbleBuffer;
    }

    @Override
    public void execute(SimpleGame model, ServerHandler server) throws IOException {
        model.setMarbleBuffer(marbleBuffer);
        Marble marble = model.getView().selectedMarble();
        DepotName depot = null;
        int selectedDepot = model.getView().selectedDepot();
        for(DepotName depotName : DepotName.values()){
            if(depotName.getPosition() == selectedDepot ){
                depot = depotName;
                break;
            }
        }

        //gestione situazione in cui la biglia è bianca
        PutResourceMsg msg = new PutResourceMsg(marble, depot, null);
        server.writeObject(msg);
    }
}
