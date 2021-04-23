package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;

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
