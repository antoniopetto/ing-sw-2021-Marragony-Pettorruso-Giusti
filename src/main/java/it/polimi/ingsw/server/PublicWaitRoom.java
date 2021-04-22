package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.messages.NPlayerMsg;
import it.polimi.ingsw.shared.messages.NPlayerRequest;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class PublicWaitRoom {

    private int nPlayers = 0;
    private Map<String, ClientHandler> players = new HashMap<>();
    private static PublicWaitRoom instance;

    public static PublicWaitRoom getInstance(){
        if (instance == null)
            instance = new PublicWaitRoom();
        return instance;
    }

    public synchronized void add(String username, ClientHandler handler) throws IOException, ClassNotFoundException {
        if (players.isEmpty()){
            handler.writeObject(new NPlayerRequest());
            nPlayers = ((NPlayerMsg) handler.readObject()).getNPlayers();
            players.put(username, handler);
        }
        else if (players.size() < nPlayers){

        }
        // lock thread until there's space left in the map
    }

}
