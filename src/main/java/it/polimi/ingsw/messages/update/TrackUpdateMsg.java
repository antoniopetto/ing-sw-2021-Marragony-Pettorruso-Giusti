package it.polimi.ingsw.messages.update;


import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.AbstractPlayer;

public class TrackUpdateMsg implements UpdateMsg {
    private String player;
    private boolean allBut;

    public TrackUpdateMsg(AbstractPlayer player, boolean allBut)
    {
        this.player=player.getUsername();
        this.allBut=allBut;
    }


    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer simplePlayer:model.getPlayers()) {
            if(!allBut)
            {
                if(simplePlayer.getUsername().equals(player))
                    simplePlayer.advance();
            }
            else {
                if (!simplePlayer.getUsername().equals(player))
                    simplePlayer.advance();
            }
        }
    }

    @Override
    public String toString() {
        return "TrackUpdateMsg{" +
                "player='" + player + '\'' +
                ", allBut=" + allBut +
                '}';
    }
}
