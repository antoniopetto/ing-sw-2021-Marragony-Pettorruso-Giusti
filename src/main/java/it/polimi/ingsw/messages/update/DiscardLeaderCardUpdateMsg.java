package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

public class DiscardLeaderCardUpdateMsg implements UpdateMsg{

    private final String username;
    private final int cardId;

    public DiscardLeaderCardUpdateMsg(String username, int cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void execute(SimpleGame game) {
        for (SimplePlayer player: game.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.discardLeaderCard(cardId);
        }
    }

    @Override
    public String toString() {
        return "DiscardLeaderCardUpdateMsg{" +
                "username='" + username + '\'' +
                ", cardId=" + cardId +
                '}';
    }
}
