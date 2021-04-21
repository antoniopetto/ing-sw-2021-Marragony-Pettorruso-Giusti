package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.shared.messages.CommandMsg;
import it.polimi.ingsw.shared.messages.TrackUpdateMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VirtualView implements Runnable{
    private Game game;
    private Map<String, PlayerHandler> players;
    private ObjectOutputStream output;
    private ObjectInputStream input;


    public VirtualView(Map<String, PlayerHandler> players)
    {
        this.players = players;
        Iterator<String> it = players.keySet().iterator();
        if(players.size()==1)
        {

            game = Game.newSinglePlayerGame(it.next());
        }
        else
        {
            List<String> usernames = new ArrayList<>();
            while (it.hasNext())
            {
                usernames.add(it.next());
            }
            game = Game.newRegularGame(usernames);
        }

    }

    @Override
    public void run() {
        while(!game.isEndgame()){
            PlayerHandler handler = players.get(game.getPlaying().getUsername());
            try {
                Object nextMsg = handler.readObject();
                CommandMsg command = (CommandMsg)nextMsg;
                command.execute(game);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void faithTrackUpdate(AbstractPlayer player, boolean allBut){
        TrackUpdateMsg msg = new TrackUpdateMsg(player, allBut);
        for (PlayerHandler handler: players.values()) {
            try {
                handler.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void vaticanReportUpdate(){}
}
