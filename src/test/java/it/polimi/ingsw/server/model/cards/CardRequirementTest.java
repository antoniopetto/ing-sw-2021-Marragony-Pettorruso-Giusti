package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.playerboard.Slot;
import it.polimi.ingsw.server.model.stubs.PlayerBoardStub;
import it.polimi.ingsw.server.model.stubs.PlayerStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CardRequirementTest {
    private CardRequirement requirement;

    private PlayerStub player;

    @Test
    public void constructorTest()
    {
        //tries to create a requirement with invalid level
        try{
            requirement = new CardRequirement(CardColor.YELLOW, 0, 1);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        //tries to create a requirement with invalid quantity
        try {
            requirement = new CardRequirement(CardColor.GREEN, 2, -3);
            fail();
        }catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        try {
            requirement= new CardRequirement(CardColor.GREEN, 2, 2);
            CardRequirement requirement1= new CardRequirement(CardColor.BLUE, 1);
        }catch (Exception e){
            fail();
        }

    }

    @Before
    public void setUp()
    {
        player=new PlayerStub("Test");
        List<Slot> slots = new ArrayList<>();

        Slot slot1 = new Slot(1);
        slot1.addCard(new DevelopmentCard(1,1, 1, CardColor.BLUE, null, null));
        slot1.addCard(new DevelopmentCard(2,1, 2, CardColor.BLUE, null, null));
        slot1.addCard(new DevelopmentCard(3,1, 3, CardColor.GREEN, null, null));
        slots.add(slot1);
        Slot slot2 = new Slot(2);
        slot2.addCard(new DevelopmentCard(4,1, 1, CardColor.GREEN, null, null));
        slot2.addCard(new DevelopmentCard(5,1, 2, CardColor.PURPLE, null, null));
        slot2.addCard(new DevelopmentCard(6,1, 3, CardColor.YELLOW, null, null));
        slots.add(slot2);
        Slot slot3 = new Slot(3);
        slot3.addCard(new DevelopmentCard(7,1, 1, CardColor.BLUE, null, null));
        slots.add(slot3);
        PlayerBoardStub playerBoard = new PlayerBoardStub(slots);
        player.setPlayerBoard(playerBoard);
    }

    @Test
    public void isSatisfiedTest()
    {
        requirement = new CardRequirement(CardColor.BLUE, 2);
        CardRequirement req1=new CardRequirement(CardColor.YELLOW, 1, 1);
        CardRequirement req2 = new CardRequirement(CardColor.PURPLE, 2);
        CardRequirement req3 = new CardRequirement(CardColor.GREEN, 2);
        CardRequirement req4 = new CardRequirement(CardColor.BLUE, 3, 2);
        assertTrue(requirement.isSatisfied(player));
        assertTrue(req1.isSatisfied(player));
        assertFalse(req2.isSatisfied(player));
        assertTrue(req3.isSatisfied(player));
        assertFalse(req4.isSatisfied(player));
    }
}