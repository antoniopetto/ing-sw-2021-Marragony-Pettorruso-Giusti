package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;

import java.util.ArrayList;
import java.util.Map;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;
    private Map<String, SimpleWarehouse> othersWarehouse;
    private final boolean[] mydeckLeaderCards = new boolean[2];
    private ArrayList<SimpleSlot> slots;

    public SimplePlayer() {
        this.slots = new ArrayList<>();
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
    }

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

    public void insertCardInSlot(int cardId, int slotId){
        slots.get(slotId).addCard(cardId);
    }
}
