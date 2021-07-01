package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.shared.Resource;

import java.util.HashSet;
import java.util.Set;

public class WhiteMarbleAliasUpdateMsg implements UpdateMsg{

    private final String username;
    private final Set<Resource> aliases;

    public WhiteMarbleAliasUpdateMsg(String username, Set<Resource> aliases){
        this.username = username;
        this.aliases = new HashSet<>(aliases);
    }

    public void execute(SimpleModel game){
        for (SimplePlayer player: game.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.setWhiteMarbleAliases(aliases);
        }
    }

    @Override
    public String toString() {
        return "WhiteMarbleAliasUpdateMsg{" +
                "username='" + username + '\'' +
                ", aliases=" + aliases +
                '}';
    }
}
