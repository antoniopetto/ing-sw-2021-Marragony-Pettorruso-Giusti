package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public interface CommandMsg {
    void execute(Game game, ClientHandler handler) throws IOException;
}
