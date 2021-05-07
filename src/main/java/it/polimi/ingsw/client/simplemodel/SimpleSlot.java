package it.polimi.ingsw.client.simplemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleSlot implements Serializable {

    private List<SimpleDevelopmentCard> cards;

    public SimpleSlot() {
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(SimpleDevelopmentCard.parse(cardId));
    }
}
