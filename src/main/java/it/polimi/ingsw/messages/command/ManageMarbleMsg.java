package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;

public class ManageMarbleMsg implements CommandMsg {

    private boolean insert;

    public ManageMarbleMsg(boolean insert) {
        this.insert = insert;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.manageMarble(insert);
    }

    @Override
    public String toString() {
        return "ManageMarbleMsg{}";
    }
}
