package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ResourceRequirementTest {
    private ResourceRequirement requirement;
    private Player player = new Player("AAA", Mockito.mock(VirtualView.class));

    @Before
    public void setUp() {
        player.getPlayerBoard().getStrongBox().addResource(Resource.COIN, 2);
        player.getPlayerBoard().getStrongBox().addResource(Resource.SHIELD, 3);
        player.getPlayerBoard().getStrongBox().addResource(Resource.STONE, 1);
    }

    @Test
    public void constructorTest(){
        //tries to create a requirement with an invalid quantity
        try {
            requirement = new ResourceRequirement(Resource.COIN, -1);
            fail();
        }catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        //tries to create a requirement with a faith resource
        try{
            requirement = new ResourceRequirement(Resource.FAITH, 4);
            fail();
        } catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        requirement = new ResourceRequirement(Resource.SERVANT, 3);
    }

    @Test
    public void isSatisfiedTest() {
        requirement = new ResourceRequirement(Resource.COIN, 1);
        ResourceRequirement r1 = new ResourceRequirement(Resource.SHIELD, 2);
        ResourceRequirement r2 = new ResourceRequirement(Resource.STONE, 2);
        ResourceRequirement r3 = new ResourceRequirement(Resource.SERVANT, 1);
        assertTrue(requirement.isSatisfied(player));
        assertTrue(r1.isSatisfied(player));
        assertFalse(r2.isSatisfied(player));
        assertFalse(r3.isSatisfied(player));
    }

}