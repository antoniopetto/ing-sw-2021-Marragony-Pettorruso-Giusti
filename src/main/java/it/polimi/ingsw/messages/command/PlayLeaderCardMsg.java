package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;

public class PlayLeaderCardMsg implements CommandMsg {

    private final int cardId;

    public PlayLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(Game game) {
        game.playLeaderCard(this.cardId);
    }

    @Override
    public String toString() {
        return "PlayLeaderCardMsg{" +
                "cardId=" + cardId +
                '}';
    }
}
