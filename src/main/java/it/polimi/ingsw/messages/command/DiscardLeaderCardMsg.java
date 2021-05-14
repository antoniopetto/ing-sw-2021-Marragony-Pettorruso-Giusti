package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;

public class DiscardLeaderCardMsg implements CommandMsg {

    private int cardId;

    public DiscardLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(Game game, ClientHandler handler) {
        game.discardLeaderCard(cardId);
    }

    @Override
    public String toString() {
        return "DiscardLeaderCardMsg{" +
                "cardId=" + cardId +
                '}';
    }
}
