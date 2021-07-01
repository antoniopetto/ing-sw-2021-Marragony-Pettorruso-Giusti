package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.CardColor;
import it.polimi.ingsw.shared.Marble;

import java.io.Serializable;
import java.util.*;

public class SimpleModel implements Serializable {

    private Integer rivalPosition;
    private List<SimplePlayer> players;
    private SimplePlayer thisPlayer;
    private List<Marble> marbleBuffer;
    private final Marble[][] marketBoard = new Marble[3][4];
    private Marble spareMarble;
    private final SimpleDevCard[][][] devCardDecks = new SimpleDevCard[4][3][4];
    private View view;
    private boolean init;

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }
    public void setView(View view){this.view=view;}
    public void setPlayers(List<SimplePlayer> players, String requirerName){

        this.players = new ArrayList<>(players);
        this.thisPlayer = getPlayer(requirerName);
    }

    public void initCards(int[][][] devCardIds, Set<Integer> leaderCardIds){

        setDevCardDecks(devCardIds);
        thisPlayer.setLeaderCards(leaderCardIds);
    }

    public void updateDevCardDecks(int level, CardColor cardColor){
        for(int i = 0; i < 4; i++) {
            if(devCardDecks[i][level - 1][0] != null && devCardDecks[i][level - 1][0].getColor() == cardColor){
                for (int j = 3; j >= 0; j--)
                    if (devCardDecks[i][level - 1][j] != null){
                        devCardDecks[i][level - 1][j] = null;
                        update("decks");
                        return;
                    }
            }
        }

    }

    public void update(String updated){if(view!=null) view.update(updated);}
    public void setMarbleBuffer(List<Marble> marbleBuffer) {
        this.marbleBuffer = new ArrayList<>(marbleBuffer);
    }

    public void reduceBuffer(Marble marble) {
        marbleBuffer.remove(marble);
    }

    public List<SimplePlayer> getPlayers() {
        return new ArrayList<>(players);
    }

    public Marble[][] getMarketBoard() {
        return marketBoard;
    }

    public void setMarketBoard(Marble[][] marketBoard) {

        for (int i = 0; i < 3; i++){
            this.marketBoard[i] = Arrays.copyOf(marketBoard[i], 4);
        }
        update("market");
    }

    public void setSpareMarble(Marble spareMarble) {
        this.spareMarble = spareMarble;
    }

    public Marble getSpareMarble() {
        return spareMarble;
    }

    public void setRivalPosition(Integer rivalPosition){
        this.rivalPosition = rivalPosition;
    }

    public Integer getRivalPosition(){
        return rivalPosition;
    }

    public void setDevCardDecks(int[][][] devCardIds){
        for(int i=0; i<4; i++)
            for(int j=0; j<3; j++)
                for(int k=0; k<4; k++){
                    if(devCardIds[i][j][k] != 0) {
                        devCardDecks[i][j][k] = SimpleDevCard.parse(devCardIds[i][j][k]);
                    }else  devCardDecks[i][j][k] = null;
                }
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
                //TODO add else result.add(null)
            }
        }
        return result;
    }

    public SimplePlayer getThisPlayer() {
        return thisPlayer;
    }

    public SimplePlayer getPlayer(String name){
        return players.stream().filter(i -> i.getUsername().equals(name)).findAny().orElseThrow(IllegalArgumentException::new);
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public boolean isInit(){
        return this.init;
    }
}
