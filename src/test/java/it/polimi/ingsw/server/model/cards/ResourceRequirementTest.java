package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.stubs.PlayerBoardStub;
import it.polimi.ingsw.server.model.stubs.PlayerStub;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResourceRequirementTest {
    private ResourceRequirement requirement;
    private PlayerStub player;

    @Test
    public void constructorTest(){
        //tries to create a requirement with an invalid quantity
        try {
            requirement=new ResourceRequirement(Resource.COIN, -1);
            fail();
        }catch(IllegalArgumentException e) {
            assertTrue(true);
        }
        //tries to create a requirement with a faith resource
        try{
            requirement=new ResourceRequirement(Resource.FAITH, 4);
            fail();
        } catch(IllegalArgumentException e)
        {
            assertTrue(true);
        }
        try
        {
            requirement=new ResourceRequirement(Resource.SERVANT, 3);
        }catch (Exception e)
        {
            fail();
        }
    }

    @Before
    public void setUp()
    {
        player=new PlayerStub("Test");
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.COIN, 2);
        resources.put(Resource.SHIELD, 3);
        resources.put(Resource.STONE, 1);
        PlayerBoardStub playerBoard= new PlayerBoardStub(resources);
        player.setPlayerBoard(playerBoard);
    }

    @Test
    public void isSatisfiedTest()
    {
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