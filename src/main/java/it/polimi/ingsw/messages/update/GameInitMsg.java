package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.List;

public class GameInitMsg implements UpdateMsg{

    private final List<SimplePlayer> players;
    private final int[][][] cardIDs;
    private final SimplePlayer thisPlayer;

    public GameInitMsg(List<SimplePlayer> players, int[][][] cardIDs, SimplePlayer thisPlayer){
        if (!players.contains(thisPlayer))
            throw new IllegalArgumentException();
        this.players = players;
        this.cardIDs = cardIDs;
        this.thisPlayer = thisPlayer;
    }

    @Override
    public void execute(SimpleGame game) {
        game.startGame(players, cardIDs, thisPlayer);
    }
}
