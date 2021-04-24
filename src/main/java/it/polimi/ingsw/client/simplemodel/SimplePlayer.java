package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.View;

import java.util.Map;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;

    public void advance()
    {
        position++;
        view.positionUpdate(this);
    }

    public String getUsername() {
        return username;
    }

    public View getView(){return view;}


    public void changeWarehouse(SimpleWarehouse warehouse)
    {
        this.warehouse = warehouse;
    }
}
