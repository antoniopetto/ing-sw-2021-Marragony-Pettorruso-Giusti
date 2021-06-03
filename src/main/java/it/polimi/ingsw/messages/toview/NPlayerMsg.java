package it.polimi.ingsw.messages.toview;

import java.io.Serializable;

public class NPlayerMsg implements Serializable {

    private final int nPlayers;

    public NPlayerMsg(int nPlayers){
        this.nPlayers = nPlayers;
    }

    public int getNPlayers(){
        return nPlayers;
    }

    @Override
    public String toString() {
        return "NPlayerMsg{" +
                "nPlayers=" + nPlayers +
                '}';
    }
}
