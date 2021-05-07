package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

public class DiscardLeaderCardUpdateMsg implements UpdateMsg{

    private String username;
    private int cardId;

    public DiscardLeaderCardUpdateMsg(String username, int cardId) {
        this.username = username;
        this.cardId = cardId;
    }

    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer player: model.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.discardLeaderCard(cardId);
        }
    }
}
