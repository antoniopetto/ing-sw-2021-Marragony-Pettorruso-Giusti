package it.polimi.ingsw.client.simplemodel;

import java.util.ArrayList;
import java.util.List;

public class SimpleSlot {

    private List<SimpleCard> cards;

    public SimpleSlot() {
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(new SimpleCard(cardId));
    }
}
