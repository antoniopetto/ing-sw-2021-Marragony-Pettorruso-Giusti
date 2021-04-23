package it.polimi.ingsw.shared.messages;


import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.AbstractPlayer;

public class TrackUpdateMsg implements ServerMsg {
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
            //It has to be modified
            if((simplePlayer.getUsername()==null&&player==null&&!allBut)||
                    (allBut&&simplePlayer.getUsername()!=null && player == null )||
                    (!allBut&&simplePlayer.getUsername().equals(player))
                    ||(allBut&&!simplePlayer.getUsername().equals(player))) {
                simplePlayer.advance();
            }

        }
    }
}
