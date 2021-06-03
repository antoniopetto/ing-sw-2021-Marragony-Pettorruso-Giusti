package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InitPlayersUpdateMsg implements UpdateMsg{

    private final List<SimplePlayer> players;
    private final String requirerName;

    public InitPlayersUpdateMsg(List<SimplePlayer> players, String requirerName){

        this.players = players;
        this.requirerName = requirerName;
    }

    @Override
    public void execute(SimpleModel game) {
        game.setPlayers(players, requirerName);
    }
}
