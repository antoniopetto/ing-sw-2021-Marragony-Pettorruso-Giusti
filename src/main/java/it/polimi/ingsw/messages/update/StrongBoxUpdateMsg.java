package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.HashMap;
import java.util.Map;

public class StrongBoxUpdateMsg implements UpdateMsg{
    private final Map<Resource, Integer> strongbox;
    private final String username;

    public StrongBoxUpdateMsg(Map<Resource, Integer> strongbox, String player) {
        this.strongbox = new HashMap<>(strongbox);
        this.username = player;
    }

    @Override
    public void execute(SimpleModel game) {
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
