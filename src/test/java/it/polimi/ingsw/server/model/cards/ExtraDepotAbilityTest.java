package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

public class ExtraDepotAbilityTest {

    @Test
    public void constructorTest()
    {
        ExtraDepotAbility ability;
        //tries to create an ability with an invalid capacity number
        try{
            ability = new ExtraDepotAbility(Resource.COIN, 0);
            fail();
        } catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        try
        {
            ability = new ExtraDepotAbility(Resource.COIN, 2);
        }catch (IllegalArgumentException e)
        {
            fail();
        }
    }

    @Test
    public void activateAbilityTest()
    {
        ExtraDepotAbility ability = new ExtraDepotAbility(Resource.COIN, 2);

        Player player = new Player("Test", Mockito.mock(VirtualView.class));
        //activates the ability and check that there's an extra depot in player's warehouse with right capacity and constraint
        try {
            ability.activateAbility(player);
            int size = player.getPlayerBoard().getWareHouse().getDepots().size();
            assertEquals(4, size);
            Depot depot = player.getPlayerBoard().getWareHouse().getDepots().get(3);
            assertEquals(Resource.COIN, depot.getConstraint());
            assertEquals(2, depot.getCapacity());

        }catch (Exception e)
        {
            fail();
        }

    }
}