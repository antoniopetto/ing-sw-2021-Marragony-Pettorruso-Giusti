package it.polimi.ingsw.client.simplemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simplified version of Slot. Simply contains a list of SimpleDevCard
 */
public class SimpleSlot implements Serializable {

    private final List<SimpleDevCard> cards;

    public SimpleSlot() {
        cards = new ArrayList<>();
    }

    public void addCard(int cardId){
        cards.add(SimpleDevCard.parse(cardId));
    }
    public void addCard(SimpleDevCard card){ cards.add(card); }

    public List<SimpleDevCard> getCards() {
        return cards;
    }

    /**
     * Returns the last card to have been added as an Optional
     * @return  The Optional of the card
     */
    public Optional<SimpleDevCard> getLastCard(){
        int size = cards.size();
        if (size == 0)
            return Optional.empty();
        else
            return Optional.of(cards.get(cards.size() - 1));
    }
}
