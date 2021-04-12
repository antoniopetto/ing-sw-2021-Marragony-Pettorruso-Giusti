package it.polimi.ingsw.model.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import static org.junit.Assert.*;

public class CardDeckTest {
    private List<DevelopmentCard> cards = new ArrayList<>();
    private CardDeck deck;

    @Test (expected = IllegalStateException.class)
    public void constructorTest(){
        try
        {
            CardDeck deck = new CardDeck(1, CardColor.GREEN);
        }catch (IllegalStateException e){
            fail();
        }
        CardDeck deck1 = new CardDeck(5, CardColor.BLUE);
        fail();


    }

    @Before
    public void inizializeCards()
    {
        cards.add(new DevelopmentCard(1,10,1, CardColor.GREEN, null, null));
        cards.add(new DevelopmentCard(2,10,2, CardColor.BLUE, null, null));
        cards.add(new DevelopmentCard(3,10,3, CardColor.YELLOW, null, null));
        cards.add(new DevelopmentCard(4,10,2, CardColor.GREEN, null, null));
        deck = new CardDeck(2, CardColor.BLUE);
    }
    @Test
    public void belongsTest(){
        assertTrue(deck.belongs(cards.get(1)));
        assertFalse(deck.belongs(cards.get(0)));
        assertFalse(deck.belongs(cards.get(2)));
        assertFalse(deck.belongs(cards.get(3)));
    }
    @After
    public void tearDown(){
        deck=null;
        cards.clear();
    }

    @Test
    public void addTest()
    {
        DevelopmentCard card1 = new DevelopmentCard(5,0, 2, CardColor.BLUE, null, null);
        DevelopmentCard card2 = new DevelopmentCard(6,0, 2, CardColor.BLUE, null, null);
        DevelopmentCard card3 = new DevelopmentCard(7, 0,2, CardColor.BLUE, null, null);
        DevelopmentCard card4 = new  DevelopmentCard(8, 0,2, CardColor.BLUE, null, null);
        DevelopmentCard card5= new DevelopmentCard(9, 0,2, CardColor.BLUE, null, null);
        CardDeck deck1 = new CardDeck(2, CardColor.BLUE);
        boolean shuffled = false;
        try {
            deck.add(cards.get(0));
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        try {
            deck.add(card1);
            deck.add(card2);
            deck.add(card3);
            deck.add(card4);
            deck1.add(card1);
            deck1.add(card2);
            deck1.add(card3);
            deck1.add(card4);
            for (int i =0; i<4; i++)
            {
                if(!deck1.pop().equals(deck.pop()))
                    shuffled = true;
            }
            assertTrue(shuffled);
        }catch (Exception e)
        {
            fail();
        }
        try {
            deck.add(card1);
            deck.add(card2);
            deck.add(card3);
            deck.add(card4);
            deck.add(card5);
            fail();
        }catch (IllegalStateException e)
        {
            assertTrue(true);
        }


    }

    @Test
    public void popTest()
    {
        try {
            deck.pop();
            fail();
        }catch (EmptyStackException e){
            assertTrue(true);
        }
        deck.add(cards.get(1));
        assertEquals(cards.get(1), deck.pop());
    }

    @Test
    public void propertiesTest()
    {
        assertTrue(deck.properties(CardColor.BLUE, 2));
        assertFalse(deck.properties(CardColor.BLUE, 1));
        assertFalse(deck.properties(CardColor.GREEN, 1));
        assertFalse(deck.properties(CardColor.YELLOW, 3));
    }

    @Test
    public void peekTest()
    {
        try{
            deck.peek();
            fail();
        }catch(EmptyStackException e)
        {
            assertTrue(true);
        }
        DevelopmentCard card1 = new DevelopmentCard(5, 0, 2, CardColor.BLUE, null, null);
        DevelopmentCard card2 = new DevelopmentCard(6, 0, 2, CardColor.BLUE, null, null);
        deck.add(card1);
        deck.add(card2);
        try {
            assertEquals(card2, deck.peek());
        }catch (EmptyStackException e)
        {
            fail();
        }
    }

    @Test
    public void sizeTest()
    {
        DevelopmentCard card1 = new DevelopmentCard(5,0, 2, CardColor.BLUE, null, null);
        DevelopmentCard card2 = new DevelopmentCard(6,0, 2, CardColor.BLUE, null, null);
        deck.add(card1);
        deck.add(card2);
        assertEquals(2, deck.size());
    }

}