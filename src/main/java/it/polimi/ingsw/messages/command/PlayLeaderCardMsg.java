package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

public class PlayLeaderCardMsg implements CommandMsg {

    private final int cardId;

    public PlayLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(Game game, ClientHandler handler) {
        game.playLeaderCard(this.cardId);
    }
}
