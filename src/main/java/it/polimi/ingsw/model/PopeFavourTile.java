package it.polimi.ingsw.model;

public class PopeFavourTile {

    private final int value;
    private boolean gained;

    PopeFavourTile(int value){
        this.value = value;
        gained = false;
    }

    public int getValue() { return value; }

    public boolean isGained() { return gained; }

    public void gain() { gained = true; }
}
