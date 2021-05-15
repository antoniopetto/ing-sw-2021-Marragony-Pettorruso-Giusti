package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public class EndInsertingMsg implements CommandMsg {
    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.endInserting();
    }
}
