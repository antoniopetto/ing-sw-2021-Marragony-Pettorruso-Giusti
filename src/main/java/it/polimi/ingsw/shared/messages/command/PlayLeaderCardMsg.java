package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;


/**
 * It is called when a player decides to active a LeaderCard with cardId id
 * @see CommandMsg
 */
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
