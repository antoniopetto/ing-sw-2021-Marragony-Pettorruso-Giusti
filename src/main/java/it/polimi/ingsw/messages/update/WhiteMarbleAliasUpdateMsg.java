package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.HashSet;
import java.util.Set;

public class WhiteMarbleAliasUpdateMsg implements UpdateMsg{

    private final Set<Resource> aliases;

    public WhiteMarbleAliasUpdateMsg(Set<Resource> aliases){
        this.aliases = new HashSet<>(aliases);
    }

    public void execute(SimpleGame game){
        game.
    }
}
