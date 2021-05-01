package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.playerboard.Slot;
import it.polimi.ingsw.server.model.stubs.PlayerBoardStub;
import it.polimi.ingsw.server.model.stubs.PlayerStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CardRequirementTest {

    private CardRequirement requirement;
    private Player player;

    @Before
    public void setUp()
    {
        List<Slot> slots = new ArrayList<>();
        ResourceRequirement requirement = new ResourceRequirement(Resource.SHIELD, 1);
        List<ResourceRequirement> requirements = new ArrayList<>();
        requirements.add(requirement);
        ProductionPower power = new ProductionPower(1, 1);
        Slot slot1 = new Slot(1);
        slot1.addCard(new DevelopmentCard(1,1, 1, CardColor.BLUE, requirements, power));
        slot1.addCard(new DevelopmentCard(2,1, 2, CardColor.BLUE, requirements, power));
        slot1.addCard(new DevelopmentCard(3,1, 3, CardColor.GREEN, requirements, power));
        slots.add(slot1);
        Slot slot2 = new Slot(2);
        slot2.addCard(new DevelopmentCard(4,1, 1, CardColor.GREEN, requirements, power));
        slot2.addCard(new DevelopmentCard(5,1, 2, CardColor.PURPLE, requirements, power));
        slot2.addCard(new DevelopmentCard(6,1, 3, CardColor.YELLOW, requirements, power));
        slots.add(slot2);
        Slot slot3 = new Slot(3);
        slot3.addCard(new DevelopmentCard(7,1, 1, CardColor.BLUE, requirements, power));
        slots.add(slot3);

        player = Mockito.mock(Player.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(player.getPlayerBoard().getSlotList()).thenReturn(slots);
    }

    @Test
    public void constructorTest()
    {
        //tries to create a requirement with invalid level
        try{
            requirement = new CardRequirement(CardColor.YELLOW, 0, 1);
            fail();
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        //tries to create a requirement with invalid quantity
        try {
            requirement = new CardRequirement(CardColor.GREEN, 2, -3);
            fail();
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            requirement= new CardRequirement(CardColor.GREEN, 2, 2);
            CardRequirement requirement1= new CardRequirement(CardColor.BLUE, 1);
        }catch (Exception e){
            fail();
        }

    }

    @Test
    public void isSatisfiedTest()
    {
        requirement = new CardRequirement(CardColor.BLUE, 2);
        CardRequirement req1 = new CardRequirement(CardColor.YELLOW, 1, 1);
        CardRequirement req2 = new CardRequirement(CardColor.PURPLE, 2);
        CardRequirement req3 = new CardRequirement(CardColor.GREEN, 2);
        CardRequirement req4 = new CardRequirement(CardColor.BLUE, 3, 2);
        assertTrue(requirement.isSatisfied(player));
        assertFalse(req1.isSatisfied(player));
        assertFalse(req2.isSatisfied(player));
        assertTrue(req3.isSatisfied(player));
        assertFalse(req4.isSatisfied(player));
    }
}