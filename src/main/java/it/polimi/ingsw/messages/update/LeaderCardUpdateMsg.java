package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;

public class LeaderCardUpdateMsg implements UpdateMsg {
    private final String username;
    private final int cardId;

    public LeaderCardUpdateMsg(String userName, int cardId) {
        this.username = userName;
        this.cardId = cardId;
    }

    @Override
    public void execute(SimpleModel game) {
        game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username)).findAny()
                .ifPresent(p -> p.activeLeaderCard(cardId));
    }

    @Override
    public String toString() {
        return "LeaderCardUpdateMsg{" +
                "username='" + username + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}