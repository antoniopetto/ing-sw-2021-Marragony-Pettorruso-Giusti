package it.polimi.ingsw.server.model.cards;


import it.polimi.ingsw.server.VirtualView;

import java.io.Serializable;
import java.util.*;


/**
 * This class represents the group of decks from which the development cards are bought. The decks are saved in the
 * ArrayList decks.
 */
public class DevelopmentCardDecks implements Serializable {

    private transient VirtualView virtualView;
    private final List<CardDeck> decks = new ArrayList<>();

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
        for (CardDeck deck: decks)
                cards.stream().filter(deck::belongs).forEach(deck::add);
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    private CardDeck getDeck(CardColor color, int level){
        if(level < 1 || level > 3)
            throw new IllegalArgumentException("Level value not valid");

        for (CardDeck deck : decks)
            if (deck.is(color, level))
                return deck;

        throw new IllegalStateException();
    }

    /**
     * @param color is the color of the deck from which the card is drawn
     * @param level is the level of the deck from which the card is drawn
     * @return the first card of the deck with those color and level.
     */
    public DevelopmentCard drawCard(CardColor color, int level) throws EmptyStackException{

        CardDeck deck = getDeck(color, level);
        DevelopmentCard card =  deck.pop();
        virtualView.devCardDecksUpdate(color, level);
        return card;
    }

    /**
     * @param color is the color of the deck from which the card is read
     * @param level is the level of the deck from which the card is read
     * @return the card on top of the deck without removing it
     */
    public DevelopmentCard readTop(CardColor color, int level) throws EmptyStackException{
        CardDeck deck = getDeck(color, level);
        return deck.peek();
    }

    /**
     * @param color is the color of the deck of which the size is required
     * @param level is the level of the deck of which the size is required
     * @return the size of the deck
     */
    public int deckSize(CardColor color, int level) {
        return getDeck(color, level).size();
    }

    /**
     * This method is used in the single player game only. It discards 2 card of the lowest level of the color passed as
     * parameter
     * @param color is the color of the two cards to be discarded
     */
    public void discard(CardColor color){

        for (int toDiscard = 2, level = 1; toDiscard > 0;){
            if (getDeck(color, level).size() > 0) {
                drawCard(color, level);
                toDiscard--;
            }
            else level ++;
            if (level == 4)
                virtualView.endSinglePlayerGame();
                return;
        }
    }

    /**
     * @return the ids of the cards in the decks. The first index is the color,
     * the second is the level and the third is the position of the card in the deck.
     */
    public int[][][] getDevCardIds() {

        int[][][] result = new int[4][3][4];
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 3; j++)
                result[i][j] = decks.get(3*i + j).getDeckIds();

        return result;
    }
}
