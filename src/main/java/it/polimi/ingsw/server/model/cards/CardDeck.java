package it.polimi.ingsw.server.model.cards;

import java.util.*;

/**
 * This class represents a deck of cards, which is used in the DevelopmentCardDecks class. It has a parameter with
 * the number of cards in the deck, attributes with the level and the color of the cards in the deck and a stack
 * attribute which contains the cards.
 */
public class CardDeck {
    private static final int num_of_cards = 4;
    private final int level;
    private final CardColor color;
    private final Stack<DevelopmentCard> deck = new Stack<>();

    public CardDeck(int level, CardColor color) {
        if (level<1||level>3) throw new IllegalArgumentException("Illegal level");
        this.level = level;
        this.color = color;
    }

    /**
     * this method checks if the level and the color of the card as parameter are the same of the deck
     * @return true if the card and the deck have the same color and level
     */
    public boolean belongs(DevelopmentCard card)
    {
        if(card.getColor().equals(this.color))
        {
            return card.getLevel() == this.level;
        }
        return false;
    }

    /**
     * The method adds a card in the stack and if the stack is full it gets shuffled.
     * @param card the card which is added
     */
    public void add(DevelopmentCard card) {
        if(!belongs(card)) throw new IllegalArgumentException("Card doesn't belong to this deck");
        if(deck.size()==num_of_cards)
            throw new IllegalStateException("The deck il full");
        deck.push(card);
        if(deck.size()==num_of_cards)
        {
            Collections.shuffle(deck);

        }


    }

    /**
     * The method remove the card on top of the stack
     * @return the card removed
     * @throws EmptyStackException if the deck is empty
     */
    public DevelopmentCard pop()  throws EmptyStackException{

        return deck.pop();
    }

    /**
     * This method checks if the deck has those color and level
     * @return true if the color and the level of the deck are the same as the parameters
     */
    public boolean properties(CardColor color, int level)
    {
        if(this.color.equals(color))
            return this.level==level;
        return false;
    }

    /**
     * @return the element on top of the deck without removing it
     * @throws EmptyStackException if the deck is empty
     */
    public DevelopmentCard peek()throws EmptyStackException{
        return deck.peek();
    }

    /**
     * @return the size of the deck
     */
    public int size()
    {
        return deck.size();
    }

    public int[] getDeckStatus()
    {
        int[] result = new int[4];
        for (int i = 0; i< 4; i++)
        {
            result[i] = deck.elementAt(i).getId();
        }
        return result;
    }
}
