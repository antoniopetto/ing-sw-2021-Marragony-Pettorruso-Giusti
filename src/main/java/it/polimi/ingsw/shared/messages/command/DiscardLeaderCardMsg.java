package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;

public class DiscardLeaderCardMsg implements CommandMsg {

    private int cardId;

    public DiscardLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(GameController gameController) {
        gameController.discardLeaderCard(cardId);
    }

    @Override
    public String toString() {
        return "DiscardLeaderCardMsg{" +
                "cardId=" + cardId +
                '}';
    }
}
