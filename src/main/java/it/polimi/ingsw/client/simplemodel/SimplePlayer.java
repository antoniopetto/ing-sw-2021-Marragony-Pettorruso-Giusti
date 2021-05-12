package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePlayer implements Serializable {
    private String username;
    private int position;
    private View view;
    private SimpleWarehouse warehouse;
    private ArrayList<SimpleSlot> slots;
    private Map<Resource, Integer> strongbox = new HashMap<>();
    private List<SimpleLeaderCard> leaderCards = new ArrayList<>();

    public SimplePlayer(String username, int[] cardIds) {
        this.username=username;
        this.slots = new ArrayList<>();
        this.position=0;
        this.warehouse = new SimpleWarehouse();
        for(int i=0; i<4; i++)
        {
            leaderCards.add(SimpleLeaderCard.parse(cardIds[i]));
        }
        slots.add(new SimpleSlot(1));
        slots.add(new SimpleSlot(2));
        slots.add(new SimpleSlot(3));


    }

    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    public void advance()
    {
        position++;
    }

    public String getUsername() {
        return username;
    }

    public View getView(){return view;}

    public void setView(View view){
        this.view = view;
    }


    public void changeWarehouse(SimpleWarehouse warehouse)
    {
        this.warehouse = warehouse;
    }

    public void activeLeaderCard(int cardId){
        for(int i = 0; i < leaderCards.size(); i++) {
            if ( leaderCards.get(i).getId() == cardId ) leaderCards.get(i).setActive(true);
        }
        view.faceUpLeaderCard(cardId);
    }

    public void insertCardInSlot(int cardId, int slotId){

        slots.get(slotId).addCard(cardId);
        view.addCardInSlot(this, cardId, slotId);
        view.showDevCardAllPlayers(cardId);

    }

    public void discardLeaderCard(int cardId){
        for(int i = 0; i < leaderCards.size(); i++) {
            if ( leaderCards.get(i).getId() == cardId ) leaderCards.remove(i);
        }
    }

    public List<SimpleLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public int chooseLeaderCard(int position){
        return leaderCards.get(position-1).getId();
    }

    public SimpleWarehouse getWarehouse() {
        return warehouse;
    }

    public int getPosition() {
        return position;
    }

    public void changeStrongbox(Map<Resource, Integer> strongbox)
    {
        this.strongbox=strongbox;
    }

    public ArrayList<SimpleSlot> getSlots() {
        return slots;
    }
}
