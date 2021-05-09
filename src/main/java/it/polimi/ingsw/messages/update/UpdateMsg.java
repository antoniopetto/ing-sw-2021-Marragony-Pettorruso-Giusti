package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;

import java.io.IOException;
import java.io.Serializable;

public interface UpdateMsg extends Serializable {

    void execute(SimpleGame model, ServerHandler server) throws IOException;
}
