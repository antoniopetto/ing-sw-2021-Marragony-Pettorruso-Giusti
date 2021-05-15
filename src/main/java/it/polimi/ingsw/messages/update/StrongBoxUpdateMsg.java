package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class StrongBoxUpdateMsg implements UpdateMsg{
    private Map<Resource, Integer> strongbox;
    private String username;

    public StrongBoxUpdateMsg(Map<Resource, Integer> strongbox, String player) {
        this.strongbox = strongbox;
        this.username = player;
    }

    @Override
    public void execute(SimpleGame game) {
        for (SimplePlayer player : game.getPlayers())
            if (player.getUsername().equals(username))
                player.changeStrongbox(strongbox);
    }

    @Override
    public String toString() {
        return "StrongBoxUpdateMsg{" +
                "strongbox=" + strongbox +
                ", username='" + username + '\'' +
                '}';
    }
}
