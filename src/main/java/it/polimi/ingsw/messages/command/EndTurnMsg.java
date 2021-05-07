package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public class EndTurnMsg implements CommandMsg{
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.endTurn();
    }
}
