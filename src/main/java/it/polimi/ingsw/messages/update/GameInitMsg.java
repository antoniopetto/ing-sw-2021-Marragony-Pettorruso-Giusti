package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.List;

public class GameInitMsg implements UpdateMsg{

    private List<SimplePlayer> players;
    private int[][][] cardIDs;
    private String handler;

    public GameInitMsg(List<SimplePlayer> players, int[][][] cardIDs, String handler){
        this.players=players;
        this.cardIDs = cardIDs;
        this.handler = handler;
    }

    @Override
    public void execute(SimpleGame model) {
        model.startGame(players, cardIDs, handler);
    }
}
