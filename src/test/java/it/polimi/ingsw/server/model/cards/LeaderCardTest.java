package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderCardTest {
    private LeaderCard card;
    private Player player;


    @Test
    public void constructorTest()
    {
        //checks that it is impossible to create a leader card with a negative number of victory points
        try {
            card = new LeaderCard(-1, null,null);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        List<Requirement> requirements = new ArrayList<>();
        SpecialAbility ability = new CardDiscountAbility(Resource.COIN, 2);
        try {
            card = new LeaderCard(1,requirements,ability);
        }catch (Exception e)
        {
            fail();
        }
    }

    @Before
    public void setUp()
    {
        SpecialAbility ability = new CardDiscountAbility(Resource.COIN, 2);
        List<Requirement> requirements = new ArrayList<>();
        card=new LeaderCard(1, requirements, ability);
        player = new Player("Test");
    }

    @Test
    public void playTest()
    {
        card.play(player);
        assertTrue(card.isPlayed());
        assertTrue(player.getActiveDiscount().containsKey(Resource.COIN));
        Integer amount = 2;
        assertEquals(amount, player.getActiveDiscount().get(Resource.COIN));
    }

    //manca is playable
}