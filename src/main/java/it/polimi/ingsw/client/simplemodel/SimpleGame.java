package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.List;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;
    private Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;
    private SimpleDevelopmentCard[][][] devCardDecks = new SimpleDevelopmentCard[3][4][4];
    private View view;
    private String thisPlayer;

    public SimpleGame(View view) {
        this.view = view;

    }

    public View getView() {
        return view;
    }

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }

    public void startGame(List<SimplePlayer> players, int[][][] cardIds, String handlerUsername) {
        view.startGame();
        this.players = players;
        thisPlayer=handlerUsername;
        for(SimplePlayer player: this.players){
            player.setView(view);
        }
        for(int i=0; i<3; i++) {
            for(int j=0; j<4; j++) {
                for(int k=0; k<4; k++) {
                    devCardDecks[i][j][k]= SimpleDevelopmentCard.parse(cardIds[i][j][k]);
                }
            }
        }
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public void updateDevCardDecks(int level, int cardColor, int deleteCard){
        devCardDecks[level][cardColor][deleteCard] = null;
    }

    public void setMarbleBuffer(List<Marble> marbles)
    {
        marbleBuffer=marbles;
        view.showMarbleBuffer(marbleBuffer);
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

    public String getThisPlayer() {
        return thisPlayer;
    }

    public SimpleDevelopmentCard[][][] getDevCardDecks() {
        return devCardDecks;
    }
}
