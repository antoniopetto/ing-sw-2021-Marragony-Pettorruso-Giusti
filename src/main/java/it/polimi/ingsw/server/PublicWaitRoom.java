package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Public wait room. All players requesting a game are put int a wait room of the appropriate size.
 * When the room is full, a new VirtualView is created and started.
 */
public class PublicWaitRoom {

    private final int nPlayers;
    private final Map<String, ClientHandler> players = new HashMap<>();

    public PublicWaitRoom (int nPlayers){
        this.nPlayers = nPlayers;
    }

    public synchronized void add(String username, ClientHandler handler) throws IOException{

        if (players.size() < nPlayers){
            players.put(username, handler);
        }

        if (players.size() == nPlayers) {
            System.out.println(nPlayers +" players game [" + Server.formatGameName(players.keySet()) + "] starting");
            new Thread(new VirtualView(players)).start();
            players.clear();
        }
    }
}
