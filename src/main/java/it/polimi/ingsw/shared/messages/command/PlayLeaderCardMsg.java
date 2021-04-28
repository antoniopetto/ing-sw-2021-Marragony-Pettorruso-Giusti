package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.shared.messages.server.ErrorMsg;

import java.io.IOException;

public class PlayLeaderCardMsg implements CommandMsg {

    private int cardId;

    public PlayLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.playLeaderCard(this.cardId);
    }
}
