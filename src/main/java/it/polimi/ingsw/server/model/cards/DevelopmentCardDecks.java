package it.polimi.ingsw.server.model.cards;

import java.util.*;


/**
 * This class represents the group of decks from which the development cards are bought. The decks are saved in the
 * ArrayList decks.
 */
public class DevelopmentCardDecks {
    private final List<CardDeck> decks=new ArrayList<>();

    /**
     * The constructor creates 12 decks with all the possible combinations of colors and levels and add each card
     * in the List received into the right deck
     * @param cards is a list of development cards which have to be divided in different decks.
     * @throws IllegalStateException if more than 4 cards in <code>cards</code> belong to a deck
     */
    public DevelopmentCardDecks(List<DevelopmentCard> cards)  throws IllegalStateException{
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
        for (CardDeck deck: decks) {
                cards.stream().filter(deck::belongs).forEach(deck::add);
        }

    }

    /**
     *
     * @param color is the color of the deck from which the card is drawn
     * @param level is the level of the deck from which the card is drawn
     * @return the first card of the deck with those color and level.
     * @throws EmptyStackException if the deck with color=<code>color</code> and level = <code>level</code> is empty
     */
    public DevelopmentCard drawCard(CardColor color, int level)  throws EmptyStackException{
        if(level<1||level>3) throw new IllegalArgumentException("Level value not valid");
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
    public DevelopmentCard readTop(CardColor color, int level) throws EmptyStackException
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
        if(level<1||level>3) throw new IllegalArgumentException("Invalid level value");
        for (CardDeck deck : decks) {
            if (deck.properties(color, level)) {
                return deck.size();
            }
        }
        return 0;
    }

    /**
     * This method is used in the single player game only. It discards 2 card of the lowest level of the color passed as
     * parameter
     * @param color is the color of the two cards to be discarded
     */
    public void discard(CardColor color){
        int level = 1;
        int toDiscard=2;
        for (CardDeck deck:decks) {
            if (deck.properties(color, level))
            {
                while(true)
                {
                    try {
                        deck.pop();
                        toDiscard--;
                        if(toDiscard==0) return;
                    }catch (EmptyStackException e)
                    {
                        level++;
                        break;
                    }
                }
            }
        }
    }
}
