package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;

public class LeaderCardUpdateMsg implements UpdateMsg {
    private final String username;
    private final int cardId;

    public LeaderCardUpdateMsg(String username, int cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void execute(SimpleModel game) {

        game.getPlayer(username).activateLeaderCard(cardId);
    }

    @Override
    public String toString() {
        return "LeaderCardUpdateMsg{" +
                "username='" + username + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}