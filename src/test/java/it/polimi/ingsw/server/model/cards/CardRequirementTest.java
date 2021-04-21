package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardRequirementTest {
    CardRequirement requirement;
    CardRequirement requirement1;
    Player player;

    @Test
    public void constructorTest()
    {
        try{
            requirement = new CardRequirement(CardColor.YELLOW, 0, 1);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        try {
            requirement = new CardRequirement(CardColor.GREEN, 2, -3);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        try {
            requirement= new CardRequirement(CardColor.GREEN, 2, 2);
            requirement1= new CardRequirement(CardColor.BLUE, 1);
        }catch (Exception e){
            fail();
        }

    }

    @Before
    public void setUp()
    {
        requirement=new CardRequirement(CardColor.BLUE, 2);
        player = new Player("Test");

    }
}