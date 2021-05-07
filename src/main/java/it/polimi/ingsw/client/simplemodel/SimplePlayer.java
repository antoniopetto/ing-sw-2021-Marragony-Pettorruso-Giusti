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
    private List<SimpleLeaderCard> chooseLCards;
    private ArrayList<SimpleSlot> slots;
    private List<SimpleLeaderCard> leaderCards = new ArrayList<>();

    public SimplePlayer(String username, int[] cardIds) {
        this.username=username;
        this.slots = new ArrayList<>();
        this.position=0;
        this.warehouse = new SimpleWarehouse();
        for(int i=0; i<4; i++)
        {
            leaderCards.add(SimpleLeaderCard.parse(i));
        }
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());


        chooseLCards = new ArrayList<>();
        chooseLCards.add(SimpleLeaderCard.parse(1));//change
        chooseLCards.add(SimpleLeaderCard.parse(1));
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
        for(int i = 0; i < 2; i++) {
            if ( chooseLCards.get(i).getId() == cardId ) chooseLCards.get(i).setActive(true);
        }
        view.faceUpLeaderCard(this, cardId);
        view.showLeaderCardAllPlayers(cardId);
    }

    public void insertCardInSlot(int cardId, int slotId){
        slots.get(slotId).addCard(cardId);
        view.addCardInSlot(this, cardId, slotId);
        view.showDevCardAllPlayers(cardId);
    }

    public void discardLeaderCard(int cardId){
        for(int i = 0; i < 2; i++) {
            if ( chooseLCards.get(i).getId() == cardId ) chooseLCards.remove(i);
        }
        view.discardLeaderCard(this, cardId);
        view.showDevCardAllPlayers(cardId);
    }
}
