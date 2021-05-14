package it.polimi.ingsw.client.simplemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleSlot implements Serializable {

    private final List<SimpleDevelopmentCard> cards;
    private final int id;

    public SimpleSlot(int id) {
        this.id=id;
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(SimpleDevelopmentCard.parse(cardId));
    }

    public List<SimpleDevelopmentCard> getCards() {
        return cards;
    }

    public int getId() {
        return id;
    }
}
