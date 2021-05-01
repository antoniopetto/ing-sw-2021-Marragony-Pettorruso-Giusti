package it.polimi.ingsw.client.simplemodel;

import java.util.ArrayList;

public class SimpleSlot {

    private ArrayList<Integer> cards;

    public SimpleSlot() {
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(cardId);
    }
}
