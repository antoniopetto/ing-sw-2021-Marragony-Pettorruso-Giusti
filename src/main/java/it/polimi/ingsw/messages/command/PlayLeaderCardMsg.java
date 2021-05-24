package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.GameController;

public class PlayLeaderCardMsg implements CommandMsg {

    private final int cardId;

    public PlayLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController gameController) {
        gameController.playLeaderCard(this.cardId);
    }

    @Override
    public String toString() {
        return "PlayLeaderCardMsg{" +
                "cardId=" + cardId +
                '}';
    }
}
