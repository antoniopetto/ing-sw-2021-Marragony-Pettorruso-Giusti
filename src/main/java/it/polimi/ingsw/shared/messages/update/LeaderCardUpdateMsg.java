package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.shared.messages.update.UpdateMsg;

public class LeaderCardUpdateMsg implements UpdateMsg {
    private final String username;
    private final int cardId;

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