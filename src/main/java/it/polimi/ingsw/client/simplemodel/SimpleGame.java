package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.ArrayList;
import java.util.List;

public class SimpleGame {
    private List<SimplePlayer> players; //the player in first position is the owner
    private List<Marble> marbleBuffer;
    private Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;
    private SimpleDevelopmentCard[][][] devCardDecks = new SimpleDevelopmentCard[4][3][4];
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
        for(int i=0; i<4; i++) {
            for(int j=0; j<3; j++) {
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

    public void updateDevCardDecks(int level, CardColor cardColor, int deleteCard){

        for(int i = 0; i < 4; i++)
        {
            if(devCardDecks[i][level][deleteCard].getColor().equals(cardColor)){
                devCardDecks[i][level][deleteCard] = null;
            }
        }
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

    public List<SimpleDevelopmentCard> getDevCardDecks() {
        SimpleDevelopmentCard[][][] decks = devCardDecks;
        List<SimpleDevelopmentCard> result = new ArrayList<>();
        for(int row = 0; row<4; row++)
        {

            for (int col = 0; col<3; col++)
            {
                int i = 3;
                while (decks[row][col][i]==null)
                {
                    i--;
                    if(i<0)break;
                }
                if(i>=0)
                {
                    result.add(decks[row][col][i]);
                }

            }
        }
        return result;
    }
}
