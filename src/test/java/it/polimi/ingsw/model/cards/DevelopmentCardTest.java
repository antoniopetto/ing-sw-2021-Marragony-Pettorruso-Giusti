package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DevelopmentCardTest {
    private DevelopmentCard card;
    private List<ResourceRequirement> list = new ArrayList<>();
    private ProductionPower power;

    @Before
    public void setUp()
    {
        list.add(new ResourceRequirement(Resource.COIN, 2));
        list.add(new ResourceRequirement(Resource.SHIELD, 1));
        Map<Resource, Integer> input = new HashMap<>();
        Map<Resource, Integer> output = new HashMap<>();
        power = new ProductionPower(1,input, output);
        card = new DevelopmentCard(1, 3,2, CardColor.BLUE, list, power);
    }

    @Test
    public void gettersTest()
    {
        //check correctness of the getters
        assertEquals(1, card.getId());
        assertEquals(3, card.getVictoryPoints());
        assertEquals(2, card.getLevel());
        assertEquals(CardColor.BLUE, card.getColor());
        assertEquals(list, card.getRequirements());
        assertEquals(power, card.getPower());
    }

    @Test
    public void isLevelHigherTest()
    {
        //checks the correctness of the method
        DevelopmentCard card1 = new DevelopmentCard(2, 1, 1, CardColor.GREEN, list, power);
        DevelopmentCard card2 = new DevelopmentCard(3, 1, 3, CardColor.YELLOW, list, power);
        DevelopmentCard card3 = new DevelopmentCard(4, 1, 2, CardColor.YELLOW, list, power);
        assertTrue(card.isLevelHigher(card1));
        assertFalse(card.isLevelHigher(card2));
        assertFalse(card.isLevelHigher(card3));
    }
}