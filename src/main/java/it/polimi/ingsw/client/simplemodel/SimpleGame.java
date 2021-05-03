package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;
import java.util.Map;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;
    private Map<Integer, Boolean> leaderCards;
    private Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;

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

    public Marble[][] getMarketBoard() {
        return marketBoard;
    }

    public void setMarketBoard(Marble[][] marketBoard) {
        this.marketBoard = marketBoard;
    }

    public Marble getSpareMarble() {
        return spareMarble;
    }
}
