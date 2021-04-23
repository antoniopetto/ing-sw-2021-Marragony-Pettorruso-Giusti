package it.polimi.ingsw.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PublicWaitRoom {

    private final int nPlayers;
    private final Map<String, ClientHandler> players = new HashMap<>();

    public PublicWaitRoom (int nPlayers){
        this.nPlayers = nPlayers;
    }

    public synchronized void add(String username, ClientHandler handler) throws IOException, ClassNotFoundException {

        if (players.size() < nPlayers)
            players.put(username, handler);

        if (players.size() == nPlayers) {
            new Thread(new VirtualView(players)).start();
            players.clear();
        }

    }

}
