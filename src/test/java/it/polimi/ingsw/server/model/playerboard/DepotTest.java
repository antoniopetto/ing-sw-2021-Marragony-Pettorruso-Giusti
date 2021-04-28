package it.polimi.ingsw.server.model.playerboard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class DepotTest {
    private Depot depot;
    private Depot extraDepot;

    @Before
    public void initDepot(){
        depot = new Depot(DepotName.LOW,DepotName.LOW.getPosition()+1);
        extraDepot = new Depot(DepotName.FIRST_EXTRA, 2, Resource.COIN);
    }

    @After
    public void clearDepot(){
        depot = null;
        extraDepot = null;
    }

    @Test
    public void testGetConstraint(){

        assertNull(depot.getConstraint());
        assertEquals(extraDepot.getConstraint(),Resource.COIN);
    }

    @Test
    public void testGetterESetter(){
        assertEquals(3, depot.getCapacity());
        assertEquals(0, depot.getQuantity());
        depot.setQuantity(2);
        assertEquals(depot.getQuantity(),2);
        try {
            depot.setQuantity(4);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        depot.setResource(Resource.SERVANT);
        assertEquals(depot.getResource(), Resource.SERVANT);

        extraDepot.setResource(Resource.COIN);
        assertEquals(extraDepot.getConstraint(),Resource.COIN);

        try {
            extraDepot.setResource(Resource.SERVANT);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        assertEquals(depot.getName(), DepotName.LOW);

    }

    @Test
    public void testGetResource(){
        try{
            depot.getResource();
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        depot.addResource(Resource.SHIELD);
        assertEquals(depot.getResource(),Resource.SHIELD);
    }

    @Test
    public void testAddResource(){

        depot.addResource(Resource.SHIELD);
        assertEquals(depot.getResource(), Resource.SHIELD);
        assertEquals(1, depot.getQuantity());

        try {
            depot.addResource(Resource.STONE);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        depot.addResource(Resource.SHIELD);
        assertEquals(2, depot.getQuantity());

        depot.addResource(Resource.SHIELD);

        try {
            depot.addResource(Resource.SHIELD);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        assertEquals(3, depot.getQuantity());

        try {
            extraDepot.addResource(Resource.SHIELD);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        extraDepot.addResource(Resource.COIN);
        assertEquals(1, extraDepot.getQuantity());
        assertEquals(Resource.COIN, extraDepot.getResource());
        assertEquals(Resource.COIN, extraDepot.getConstraint());
    }

    @Test
    public void testRemoveResourceFromDepot(){

        try {
            depot.removeResource();
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        assertEquals(depot.getQuantity(), 0);
        depot.addResource(Resource.COIN);
        assertEquals(depot.getQuantity(), 1);
        depot.removeResource();
        assertEquals(depot.getQuantity(),0);

    }

    @Test
    public void testIsEmpty(){
        assertTrue(depot.isEmpty());
        assertTrue(extraDepot.isEmpty());

        depot.addResource(Resource.SHIELD);
        assertFalse(depot.isEmpty());
        extraDepot.addResource(Resource.COIN);
        assertFalse(depot.isEmpty());

    }

    @Test
    public void testIsFull(){
        assertFalse(depot.isFull());
        depot.addResource(Resource.STONE);
        depot.addResource(Resource.STONE);
        assertFalse(depot.isFull());
        depot.addResource(Resource.STONE);
        assertTrue(depot.isFull());

    }


}