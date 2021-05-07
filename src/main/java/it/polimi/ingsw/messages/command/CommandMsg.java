package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.io.Serializable;

public interface CommandMsg extends Serializable {
    void execute(Game game, ClientHandler handler) throws IOException;
}
