package it.polimi.ingsw.shared;

import java.io.Serializable;

/**
 * Represents a pope favour tile, with its value and its gained status
 */
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
