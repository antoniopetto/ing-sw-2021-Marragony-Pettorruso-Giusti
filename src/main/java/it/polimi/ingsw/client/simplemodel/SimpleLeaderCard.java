package it.polimi.ingsw.client.simplemodel;

public class SimpleLeaderCard {
    private boolean isPlayed;

    public SimpleLeaderCard() {
        this.isPlayed = false;
    }

    public void play(){
        this.isPlayed = true;
    }
}
