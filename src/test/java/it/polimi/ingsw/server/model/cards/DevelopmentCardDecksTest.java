package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import static org.junit.Assert.*;

public class DevelopmentCardDecksTest {
    private final List<DevelopmentCard> cards = new ArrayList<>();
    private DevelopmentCardDecks decks;
    ProductionPower power;
    List<ResourceRequirement> requirements = new ArrayList<>();

    @Before
    public void setUp() {
        //generates a list of DevelopmentCard to create a DevelopmentCardDecks with a card in every deck and 4 cards in
        //the BLUE 1 deck
        ResourceRequirement requirement = new ResourceRequirement(Resource.SHIELD, 1);
        requirements.add(requirement);
        power = new ProductionPower(1, 1);
        cards.add(new DevelopmentCard(1,0, 1, CardColor.BLUE, requirements,power));
        cards.add(new DevelopmentCard(2,0, 2, CardColor.BLUE, requirements,power));
        cards.add(new DevelopmentCard(3,0, 3, CardColor.BLUE, requirements,power));
        cards.add(new DevelopmentCard(4,0, 1, CardColor.YELLOW, requirements,power));
        cards.add(new DevelopmentCard(5,0, 2, CardColor.YELLOW, requirements,power));
        cards.add(new DevelopmentCard(6,0, 3, CardColor.YELLOW, requirements,power));
        cards.add(new DevelopmentCard(7,0, 1, CardColor.PURPLE, requirements,power));
        cards.add(new DevelopmentCard(8,0, 2, CardColor.PURPLE, requirements,power));
        cards.add(new DevelopmentCard(9,0, 3, CardColor.PURPLE, requirements,power));
        cards.add(new DevelopmentCard(10,0, 1, CardColor.GREEN, requirements,power));
        cards.add(new DevelopmentCard(11,0, 2, CardColor.GREEN, requirements,power));
        cards.add(new DevelopmentCard(12,0, 3, CardColor.GREEN, requirements,power));
        cards.add(new DevelopmentCard(13,0, 1, CardColor.BLUE, requirements,power));
        cards.add(new DevelopmentCard(14,0, 1, CardColor.BLUE, requirements,power));
        cards.add(new DevelopmentCard(15,0, 1, CardColor.BLUE, requirements,power));
        decks = new DevelopmentCardDecks(cards, Mockito.mock(VirtualView.class));
    }
    @After
    public void tearDown()
    {
        cards.clear();
    }
    @Test
    public void constructorTest()
    {
        try
        {
            DevelopmentCardDecks decks1 = new DevelopmentCardDecks(cards, null);
        }catch(IllegalStateException e)
        {
            fail();
        }
        //tries to create a DevelopmentCardDecks with 5 cards in a deck
        cards.add(new DevelopmentCard(16,0, 1, CardColor.BLUE, requirements, power));
        try
        {
            DevelopmentCardDecks decks2 = new DevelopmentCardDecks(cards, null);
            fail();
        }catch (IllegalStateException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void drawCardTest(){
        DevelopmentCard card = decks.drawCard(CardColor.YELLOW, 3);
        assertEquals(6, card.getId());
        //tries to draw a card of level 4, which is impossible
        try
        {
            decks.drawCard(CardColor.BLUE, 4);
            fail();
        }catch(IllegalArgumentException e)
        {
            assertTrue(true);
        }
        //tries to draw a card from an empty deck
        try
        {
            decks.drawCard(CardColor.YELLOW, 3);
            fail();
        }catch (EmptyStackException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void readTopTest()
    {
        DevelopmentCard card = decks.readTop(CardColor.PURPLE, 2);
        assertEquals(8, card.getId());
        //checks if the card has been removed from the deck after readTop
        try{
            decks.drawCard(CardColor.PURPLE, 2);
        }catch (EmptyStackException e)
        {
            fail();
        }
        //tries to read the top of an empty deck
        try{
            decks.readTop(CardColor.PURPLE, 2);
            fail();
        }catch (EmptyStackException e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void deckSizeTest()
    {
        //tries to get the size of a deck of level 4 (impossible)
        try
        {
            decks.deckSize(CardColor.PURPLE, 4);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        //checks correctness of the method
        assertEquals(4, decks.deckSize(CardColor.BLUE, 1));
    }

    @Test
    public void discardTest()
    {
        //checks correctness of the method
        decks.discard(CardColor.YELLOW);
        assertTrue(decks.deckSize(CardColor.YELLOW,1)==0&&decks.deckSize(CardColor.YELLOW,2)==0);
        assertNotEquals(0, decks.deckSize(CardColor.YELLOW, 3));
        decks.drawCard(CardColor.GREEN,1);
        decks.drawCard(CardColor.GREEN,2);
        decks.drawCard(CardColor.GREEN,3);
        decks.discard(CardColor.GREEN);
        assertTrue(true);
    }

    @Test
    public void getDecksStatusTest()
    {
        int[][][] result = decks.getDecksStatus();
        assertEquals(11, result[0][1][0]);
        assertEquals(9, result[3][2][0]);
        assertEquals(0, result[3][2][3]);
    }
}

