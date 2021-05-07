package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.List;

public class GameInitMsg implements UpdateMsg{

    private List<SimplePlayer> players;
    private int[][][] cardIDs;
    public GameInitMsg(List<SimplePlayer> players, int[][][] cardIDs){
        this.players=players;
        this.cardIDs = cardIDs;
    }

    @Override
    public void execute(SimpleGame model) {
        model.startGame(players, cardIDs);
    }
}
