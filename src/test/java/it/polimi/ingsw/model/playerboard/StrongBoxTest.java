package it.polimi.ingsw.model.playerboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StrongBoxTest {
    private StrongBox strongBox;

    @Before
    public void initStrongBox(){
        strongBox = new StrongBox();
    }

    @After
    public void clearStrongBox() {
        strongBox = null;
    }

    @Test
    public void testGetQuantity(){
        assertEquals(0,strongBox.getQuantity(Resource.COIN));

        strongBox.addResource(Resource.COIN, 5);
        strongBox.addResource(Resource.SERVANT,3);
        strongBox.addResource(Resource.COIN,2);

        assertEquals(7, strongBox.getQuantity(Resource.COIN));

    }

    @Test
    public void testAddResource(){
        strongBox.addResource(Resource.COIN, 5);
        assertEquals(5, strongBox.getQuantity(Resource.COIN));

        strongBox.addResource(Resource.COIN, 7);
        assertEquals(12, strongBox.getQuantity(Resource.COIN));

        try{
            strongBox.addResource(Resource.SHIELD, 0);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

    }

    @Test
    public void testRemoveResource(){
        try{
            strongBox.removeResource(Resource.COIN);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        strongBox.addResource(Resource.COIN, 1);
        strongBox.removeResource(Resource.COIN);
        assertEquals(0, strongBox.getQuantity(Resource.COIN));

        strongBox.addResource(Resource.SHIELD, 7);
        strongBox.removeResource(Resource.SHIELD);
        strongBox.removeResource(Resource.SHIELD);
        assertEquals(5, strongBox.getQuantity(Resource.SHIELD));

    }

    @Test
    public void testCountTotalResources(){

        assertEquals(0,strongBox.countTotalResources());

        strongBox.addResource(Resource.COIN, 5);
        strongBox.addResource(Resource.SHIELD, 12);
        strongBox.addResource(Resource.SERVANT, 10);
        strongBox.addResource(Resource.STONE, 7);

        assertEquals(34, strongBox.countTotalResources());

    }

}