package it.polimi.ingsw.model.cards;

import java.util.*;


/**
 * This class represents the group of decks from which the development cards are bought. The decks are saved in the
 * ArrayList decks.
 */
public class DevelopmentCardDecks {
    private final List<CardDeck> decks=new ArrayList<CardDeck>();

    /**
     * The constructor creates 12 decks with all the possible combinations of colors and levels and add each card
     * in the List received into the right deck
     * @param cards is a list of development cards which have to be divided in different decks.
     */
    public DevelopmentCardDecks(List<DevelopmentCard> cards) {
        Iterator<DevelopmentCard> iterator = cards.iterator();
        decks.add(new CardDeck(1, CardColor.GREEN));
        decks.add(new CardDeck(2, CardColor.GREEN));
        decks.add(new CardDeck(3, CardColor.GREEN));
        decks.add(new CardDeck(1, CardColor.BLUE));
        decks.add(new CardDeck(2, CardColor.BLUE));
        decks.add(new CardDeck(3, CardColor.BLUE));
        decks.add(new CardDeck(1, CardColor.YELLOW));
        decks.add(new CardDeck(2, CardColor.YELLOW));
        decks.add(new CardDeck(3, CardColor.YELLOW));
        decks.add(new CardDeck(1, CardColor.PURPLE));
        decks.add(new CardDeck(2, CardColor.PURPLE));
        decks.add(new CardDeck(3, CardColor.PURPLE));
        while(iterator.hasNext())
        {
            DevelopmentCard card = iterator.next();
            for (CardDeck deck: decks) {
                if(deck.belongs(card)) {
                    deck.add(card);
                    break;
                }
            }
        }
    }

    /**
     *
     * @param color is the color of the deck from which the card is drawn
     * @param level is the level of the deck from which the card is drawn
     * @return the first card of the deck with those color and level.
     */
    public DevelopmentCard drawCard(CardColor color, int level){
        for (CardDeck deck : decks) {
            if (deck.properties(color, level)) {
                return deck.pop();
            }
        }
        return null;
    }

    /**
     *
     * @param color is the color of the deck from which the card is read
     * @param level is the level of the deck from which the card is read
     * @return the card on top of the deck without removing it
     */
    public DevelopmentCard readTop(CardColor color, int level)
    {
        for (CardDeck deck : decks) {
            if (deck.properties(color, level)) {
                return deck.peek();
            }
        }
        return null;
    }

    /**
     *
     * @param color is the color of the deck of which the size is required
     * @param level is the level of the deck of which the size is required
     * @return the size of the deck
     */
    public int deckSize(CardColor color, int level)
    {
        for (CardDeck deck : decks) {
            if (deck.properties(color, level)) {
                return deck.size();
            }
        }
        return 0;
    }
}
