package it.polimi.ingsw.client.simplemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleSlot implements Serializable {

    private final List<SimpleDevCard> cards;

    public SimpleSlot(int id) {
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(SimpleDevCard.parse(cardId));
    }

    public List<SimpleDevCard> getCards() {
        return cards;
    }

    public Optional<SimpleDevCard> getLastCard(){
        int size = cards.size();
        if (size == 0)
            return Optional.empty();
        else
            return Optional.of(cards.get(cards.size() - 1));
    }
}
