package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimplePlayer {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;
    private Map<String, SimpleWarehouse> othersWarehouse;
    private boolean[] mydeckLeaderCards;
    private ArrayList<SimpleSlot> slots;
    private int leaderCardPresent;
    private List<SimpleLeaderCard> leaderCards = new ArrayList<>();

    public SimplePlayer(String username, int[] cardIds) {
        this.username=username;
        this.slots = new ArrayList<>();
        this.position=0;
        this.warehouse = new SimpleWarehouse();
        for(int i=0; i<4; i++)
        {
            leaderCards.add(new SimpleLeaderCard(cardIds[i]));
        }
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());

        //old
        leaderCardPresent = 2;
        mydeckLeaderCards = new boolean[leaderCardPresent];
    }

    public void advance()
    {
        position++;
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
        view.faceUpLeaderCard(this, cardId);
        view.showLeaderCardAllPlayers(cardId);
    }

    public void insertCardInSlot(int cardId, int slotId){
        slots.get(slotId).addCard(cardId);
        view.addCardInSlot(this, cardId, slotId);
        view.showDevCardAllPlayers(cardId);
    }

    public void discardLeaderCard(int cardId){
        leaderCardPresent--;
        mydeckLeaderCards = new boolean[leaderCardPresent];
        view.discardLeaderCard(this, cardId);
        view.showDevCardAllPlayers(cardId);
    }
}
