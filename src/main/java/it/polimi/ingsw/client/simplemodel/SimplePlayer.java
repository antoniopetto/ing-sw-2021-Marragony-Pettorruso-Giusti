package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.View;

import java.util.Map;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;
    private Map<String, SimpleWarehouse> othersWarehouse;
    private final SimpleLeaderCard[] simpleLeaderCards = new SimpleLeaderCard[2];


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
        simpleLeaderCards[cardId].play();
        view.faceUpLeaderCard(this);
    }
}
