package it.polimi.ingsw.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PublicWaitRoom {

    private int nPlayers = 0;
    private Map<String, Socket> playerNames = new HashMap<>();
    private static PublicWaitRoom instance;

    public static PublicWaitRoom getInstance(){
        if (instance == null)
            instance = new PublicWaitRoom();
        return instance;
    }


}
