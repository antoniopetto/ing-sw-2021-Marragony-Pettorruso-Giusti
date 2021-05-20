package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InitPlayersUpdateMsg implements UpdateMsg{

    private final List<SimplePlayer> players = new ArrayList<>();
    private SimplePlayer thisPlayer;

    public InitPlayersUpdateMsg(Set<String> usernames, String thisUsername){
        if (!usernames.contains(thisUsername))
            throw new IllegalArgumentException();

        for (String username : usernames) {
            SimplePlayer newPlayer = new SimplePlayer(username);
            players.add(newPlayer);
            if (username.equals(thisUsername))
                thisPlayer = newPlayer;
        }
    }

    @Override
    public void execute(SimpleGame game) {
        game.createPlayers(players, thisPlayer);
    }
}
