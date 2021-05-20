package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleGame {
    private List<SimplePlayer> players;
    private List<Marble> marbleBuffer;
    private Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;
    private final SimpleDevCard[][][] devCardDecks = new SimpleDevCard[4][3][4];
    private final View view;
    private SimplePlayer thisPlayer;

    public SimpleGame(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }

    public void createPlayers(List<SimplePlayer> players, SimplePlayer thisPlayer){
        if (!players.contains(thisPlayer))
            throw new IllegalArgumentException();
        this.players = players;
        this.thisPlayer = thisPlayer;
    }

    public void initCards(int[][][] devCardIds, Set<Integer> leaderCardIds){
        for(int i=0; i<4; i++) {
            for(int j=0; j<3; j++) {
                for(int k=0; k<4; k++) {
                    devCardDecks[i][j][k]= SimpleDevCard.parse(devCardIds[i][j][k]);
                }
            }
        }
        thisPlayer.addLeaderCards(leaderCardIds);
        //TODO maybe needed? Was uncommented before
        /*try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/
    }

    public void updateDevCardDecks(int level, CardColor cardColor){

        for(int i = 0; i < 4; i++) {
            if(devCardDecks[i][level][0] != null && devCardDecks[i][level][0].getColor().equals(cardColor)){
                int size = devCardDecks[i][level].length;
                devCardDecks[i][level][size-1] = null;
            }
        }
    }

    public void setMarbleBuffer(List<Marble> marbles) {
        marbleBuffer = marbles;
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

    public SimplePlayer getThisPlayer() {
        return thisPlayer;
    }

    public List<SimpleDevCard> getDevCardDecks() {
        SimpleDevCard[][][] decks = devCardDecks;
        List<SimpleDevCard> result = new ArrayList<>();
        for(int row = 0; row<4; row++) {

            for (int col = 0; col<3; col++) {
                int i = 3;
                while (decks[row][col][i]==null) {
                    i--;
                    if(i<0)break;
                }
                if(i>=0) {
                    result.add(decks[row][col][i]);
                }
            }
        }
        return result;
    }
}
