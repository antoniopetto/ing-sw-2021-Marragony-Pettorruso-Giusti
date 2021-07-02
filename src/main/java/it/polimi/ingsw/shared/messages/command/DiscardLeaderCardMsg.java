package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;


/**
 * It is called when a player decides to discard a LeaderCard with cardId id
 * @see CommandMsg
 */
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
