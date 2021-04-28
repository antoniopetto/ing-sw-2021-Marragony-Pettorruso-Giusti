package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

public class LeaderCardUpdateMsg implements ServerMsg {
    private String username;
    private int cardId;

    public LeaderCardUpdateMsg(String userName, int cardId) {
        this.username = userName;
        this.cardId = cardId;
    }

    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer player: model.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.activeLeaderCard(cardId);
        }
    }
}
