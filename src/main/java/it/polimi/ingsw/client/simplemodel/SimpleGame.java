package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;
import java.util.Map;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;
    private Map<Integer, Boolean> leaderCards;

    public void setMarbleBuffer(List<Marble> marbles)
    {
        marbleBuffer=marbles;
    }

    public void reduceBuffer(Marble marble)
    {
        marbleBuffer.remove(marble);
    }

    public List<SimplePlayer> getPlayers() {
        return players;
    }
}
