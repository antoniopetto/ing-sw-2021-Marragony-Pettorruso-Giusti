package it.polimi.ingsw.messages.view;

import java.io.Serializable;

public class NPlayerMsg implements Serializable {

    private final int nPlayers;

    public NPlayerMsg(int nPlayers){
        if (nPlayers < 1 || nPlayers > 4){
            throw new IllegalArgumentException("Cannot request a multiplayer game with" + nPlayers + "players");
        }
        this.nPlayers = nPlayers;
    }

    public int getNPlayers(){
        return nPlayers;
    }

}