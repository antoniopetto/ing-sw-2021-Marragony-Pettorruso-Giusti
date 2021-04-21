package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProductionPowerTest {

    @Test
    public void constructorAndGettersTest()
    {
        Map<Resource, Integer> input = new HashMap<>();
        input.put(Resource.COIN, 2);
        Map<Resource,Integer> output = new HashMap<>();
        output.put(Resource.SHIELD, 3);
        try
        {
            ProductionPower p1 = new ProductionPower(1,2);
            ProductionPower p2 = new ProductionPower(input, output);
            ProductionPower p3 = new ProductionPower(input, output, 1, 2);
            assertTrue(p1.getInputResources().isEmpty());
            assertTrue(p1.getOutputResources().isEmpty());
            assertEquals(1, p1.getAgnosticInput());
            assertEquals(2, p1.getAgnosticOutput());
            assertEquals(input.get(Resource.COIN), p2.getInputResources().get(Resource.COIN));
            assertEquals(output.get(Resource.SHIELD), p2.getOutputResources().get(Resource.SHIELD));
            assertEquals(1, p3.getAgnosticInput());
            assertEquals(2, p3.getAgnosticOutput());
            assertEquals(input.get(Resource.COIN), p3.getInputResources().get(Resource.COIN));
            assertEquals(output.get(Resource.SHIELD), p3.getOutputResources().get(Resource.SHIELD));
        } catch(IllegalArgumentException e)
        {
            fail();
        }
        input.put(Resource.FAITH, 2);
        try{
            ProductionPower p4 = new ProductionPower(input, output);
            fail();
        }catch(IllegalArgumentException e)
        {
            assertTrue(true);
        }

        try {
            ProductionPower p5 = new ProductionPower(-1,-1);
            fail();
        }catch (IllegalArgumentException e )
        {
            assertTrue(true);
        }

    }
}