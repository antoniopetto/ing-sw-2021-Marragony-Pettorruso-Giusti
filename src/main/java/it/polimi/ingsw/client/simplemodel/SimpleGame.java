package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;
import java.util.Map;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;
    private Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;
    private SimpleCard[][][] devCardDecks = new SimpleCard[3][4][4];
    private View view;

    public SimpleGame(View view) {
        this.view = view;

    }

    public void startGame(List<SimplePlayer> players, int[][][] cardIds)
    {
        view.startGame();
        this.players = players;
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                for(int k=0; k<4; k++)
                {
                    devCardDecks[i][j][k]= new SimpleCard(cardIds[i][j][k]);
                }
            }
        }
    }

    public void updateDevCardDecks(int level, int cardColor, int deleteCard){
        devCardDecks[level][cardColor][deleteCard] = null;
    }

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

    public void setSpareMarble(Marble spareMarble) {
        this.spareMarble = spareMarble;
    }

    public Marble getSpareMarble() {
        return spareMarble;
    }
}
