package it.polimi.ingsw.server.model.shared;

import java.io.Serializable;

public class PopeFavourTile implements Serializable {

    private final int value;
    private boolean gained;

    public PopeFavourTile(int value){
        this.value = value;
        gained = false;
    }

    public int getValue() { return value; }

    public boolean isGained() { return gained; }

    public void gain() { gained = true; }
}
