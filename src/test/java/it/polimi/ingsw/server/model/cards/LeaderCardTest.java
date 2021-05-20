package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.playerboard.Slot;
import it.polimi.ingsw.server.model.stubs.PlayerBoardStub;
import it.polimi.ingsw.server.model.stubs.PlayerStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LeaderCardTest {
    private LeaderCard card;
    private PlayerStub player;
    SpecialAbility ability;


    @Test
    public void constructorTest()
    {
        //checks that it is impossible to create a leader card with a negative number of victory points
        try {
            card = new LeaderCard(1, -1, null,null);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        //checks that it is impossible to create a leader card without requirement or power
        try{
            card=new LeaderCard(1, 2, null, null);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        List<Requirement> requirements = new ArrayList<>();
        SpecialAbility ability = new CardDiscountAbility(Resource.COIN, 2);
        try {
            card = new LeaderCard(2, 1,requirements,ability);
        }catch (Exception e)
        {
            fail();
        }
    }

    @Before
    public void setUp()
    {
        ability = new CardDiscountAbility(Resource.COIN, 2);
        List<ResourceRequirement> requirements = new ArrayList<>();
        ResourceRequirement requirement = new ResourceRequirement(Resource.SHIELD, 1);
        requirements.add(requirement);
        card=new LeaderCard(3,1, requirements, ability);
        player = new PlayerStub("Test");
        List<Slot> slots = new ArrayList<>();
        ProductionPower power = new ProductionPower(1, 1);
        Slot slot1 = new Slot();
        slot1.addCard(new DevelopmentCard(1,1, 1, CardColor.BLUE, requirements, power));
        slot1.addCard(new DevelopmentCard(2,1, 2, CardColor.BLUE, requirements, power));
        slot1.addCard(new DevelopmentCard(3,1, 3, CardColor.GREEN, requirements, power));
        slots.add(slot1);
        Slot slot2 = new Slot();
        slot2.addCard(new DevelopmentCard(4,1, 1, CardColor.GREEN, requirements, power));
        slot2.addCard(new DevelopmentCard(5,1, 2, CardColor.PURPLE, requirements, power));
        slot2.addCard(new DevelopmentCard(6,1, 3, CardColor.YELLOW, requirements, power));
        slots.add(slot2);
        Slot slot3 = new Slot();
        slot3.addCard(new DevelopmentCard(7,1, 1, CardColor.BLUE, requirements, power));
        slots.add(slot3);
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.COIN, 2);
        resources.put(Resource.SHIELD, 3);
        resources.put(Resource.STONE, 1);
        PlayerBoardStub playerBoard= new PlayerBoardStub(resources, slots);
        player.setPlayerBoard(playerBoard);
    }

    @Test
    public void playTest()
    {
        try{
            card.play(player);
        }catch (NullPointerException e)
        {
            assertTrue(true);
        }catch (Exception e1)
        {
            fail();
        }
        assertTrue(card.isPlayed());
        assertTrue(player.getActiveDiscount().containsKey(Resource.COIN));
        Integer amount = 2;
        assertEquals(amount, player.getActiveDiscount().get(Resource.COIN));
    }

    @Test
    public void isPlayableTest()
    {
        assertTrue(card.isPlayable(player));
        List<CardRequirement> requirements = new ArrayList<>();
        requirements.add(new CardRequirement(CardColor.BLUE, 2));
        requirements.add((new CardRequirement(CardColor.GREEN,3,1)));
        LeaderCard card1 = new LeaderCard(1,1,requirements, ability);
        assertTrue(card1.isPlayable(player));

        List<ResourceRequirement> resRequirements = new ArrayList<>();
        resRequirements.add(new ResourceRequirement(Resource.SERVANT,1));
        resRequirements.add(new ResourceRequirement(Resource.SHIELD, 2));
        LeaderCard card2 = new LeaderCard(1,1, resRequirements, ability);
        assertFalse(card2.isPlayable(player));
    }
}