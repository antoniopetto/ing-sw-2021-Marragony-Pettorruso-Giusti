package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.views.View;

import java.util.Map;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;
    private Map<String, SimpleWarehouse> othersWarehouse;
    private final boolean[] mydeckLeaderCards = new boolean[2];


    public void advance()
    {
        position++;
        view.positionUpdate(this);
    }

    public String getUsername() {
        return username;
    }

    public View getView(){return view;}

    public void changeOthersState(String player, SimpleWarehouse warehouse)
    {
        othersWarehouse.replace(player, warehouse);
    }

    public void changeWarehouse(SimpleWarehouse warehouse)
    {
        this.warehouse = warehouse;
    }

    public void activeLeaderCard(int cardId){
        mydeckLeaderCards[cardId] = true;
        view.faceUpLeaderCard(this);
        view.showLeaderCardAllPlayers(cardId);
    }
}
