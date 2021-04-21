package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.View;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;


    public void advance()
    {
        position++;
        view.positionUpdate(this);
    }

    public String getUsername() {
        return username;
    }
}
