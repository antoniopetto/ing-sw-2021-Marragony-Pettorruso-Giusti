package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.shared.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Update Message that sets the contents of the strongbox of a SimplePlayer
 */
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
