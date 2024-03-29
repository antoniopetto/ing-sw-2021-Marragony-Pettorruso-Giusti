package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class WareHouseTest {

    private WareHouse wareHouse;

    @Before
    public void initWareHouse(){
        wareHouse = new WareHouse();
        wareHouse.setVirtualView(Mockito.mock(VirtualView.class));
    }

    @Test
    public void testCreateExtraDepot(){
        wareHouse.createExtraDepot(Resource.COIN, 4);
        assertEquals(4,wareHouse.getDepots().size());
        wareHouse.createExtraDepot(Resource.SHIELD,5);
        assertEquals(5,wareHouse.getDepots().size());

        try{
            wareHouse.createExtraDepot(Resource.FAITH,2);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void testInsert(){
        wareHouse.insert(DepotName.LOW, Resource.COIN);
        assertEquals(1, wareHouse.depotByName(DepotName.LOW).getQuantity());
        assertEquals(Resource.COIN, wareHouse.depotByName(DepotName.LOW).getResource());

        wareHouse.insert(DepotName.LOW, Resource.COIN);
        assertEquals(2, wareHouse.depotByName(DepotName.LOW).getQuantity());

        try{
            wareHouse.insert(DepotName.LOW, Resource.SHIELD);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        wareHouse.createExtraDepot(Resource.COIN, 3);
        try{
            wareHouse.insert(DepotName.FIRST_EXTRA, Resource.STONE);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        wareHouse.insert(DepotName.FIRST_EXTRA, Resource.COIN);

        wareHouse.insert(DepotName.LOW, Resource.COIN);
        try {
            wareHouse.insert(DepotName.MEDIUM, Resource.COIN);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        try{
            wareHouse.insert(DepotName.LOW, Resource.COIN);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        assertEquals(3, wareHouse.depotByName(DepotName.LOW).getQuantity());
        assertEquals(1, wareHouse.depotByName(DepotName.FIRST_EXTRA).getQuantity());

    }

    @Test
    public void testSwitchDepots(){
        wareHouse.createExtraDepot(Resource.COIN, 2);
        wareHouse.insert(DepotName.HIGH, Resource.COIN);
        //da finire, prima scegliere una soluzione per compareTwoDepots

    }

    @Test
    public void testRemoveResource(){

        try {
            wareHouse.removeResource(Resource.COIN);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        wareHouse.insert(DepotName.MEDIUM, Resource.COIN);
        wareHouse.insert(DepotName.MEDIUM, Resource.COIN);
        assertEquals(2, wareHouse.depotByName(DepotName.MEDIUM).getQuantity());
        wareHouse.insert(DepotName.HIGH, Resource.SHIELD);
        wareHouse.removeResource(Resource.COIN);
        assertEquals(1, wareHouse.depotByName(DepotName.MEDIUM).getQuantity());
        assertEquals(1, wareHouse.depotByName(DepotName.HIGH).getQuantity());

        wareHouse.createExtraDepot(Resource.COIN,1);
        wareHouse.insert(DepotName.FIRST_EXTRA,Resource.COIN);
        assertEquals(1, wareHouse.depotByName(DepotName.FIRST_EXTRA).getQuantity());
        wareHouse.removeResource(Resource.COIN);
        assertEquals(0, wareHouse.depotByName(DepotName.MEDIUM).getQuantity());
        assertEquals(1, wareHouse.depotByName(DepotName.FIRST_EXTRA).getQuantity());
        wareHouse.removeResource(Resource.COIN);
        assertEquals(0, wareHouse.depotByName(DepotName.FIRST_EXTRA).getQuantity());
        assertEquals(1, wareHouse.depotByName(DepotName.HIGH).getQuantity());
    }

    @Test
    public void testTotalResourceOfAType(){
        assertEquals(0, wareHouse.totalResourcesOfAType(Resource.SHIELD));

        wareHouse.createExtraDepot(Resource.COIN, 3);
        wareHouse.insert(DepotName.MEDIUM, Resource.COIN);
        wareHouse.insert(DepotName.FIRST_EXTRA, Resource.COIN);
        wareHouse.insert(DepotName.FIRST_EXTRA, Resource.COIN);

        assertEquals(3, wareHouse.totalResourcesOfAType(Resource.COIN));

    }

    @Test
    public void testCountTotalResources(){
        //controllo ulteriore per il depotFilter vuoto
        assertEquals(0, wareHouse.countTotalResources());

        wareHouse.insert(DepotName.LOW,Resource.COIN);
        wareHouse.insert(DepotName.MEDIUM,Resource.SHIELD);
        wareHouse.createExtraDepot(Resource.SHIELD, 4);
        wareHouse.insert(DepotName.FIRST_EXTRA, Resource.SHIELD);

        assertEquals(3, wareHouse.countTotalResources());

    }

    @Test
    public void moveDepotsTest() throws Exception {

        WareHouse wareHouse = new WareHouse();
        wareHouse.setVirtualView(Mockito.mock(VirtualView.class));
        wareHouse.createExtraDepot(Resource.SERVANT, 2);
        wareHouse.insert(DepotName.HIGH, Resource.SERVANT);
        wareHouse.moveDepots(DepotName.HIGH, DepotName.FIRST_EXTRA);
        wareHouse.insert(DepotName.MEDIUM, Resource.SERVANT);
        wareHouse.moveDepots(DepotName.FIRST_EXTRA, DepotName.MEDIUM);
        wareHouse.insert(DepotName.LOW, Resource.COIN);
        wareHouse.insert(DepotName.LOW, Resource.COIN);
        wareHouse.switchDepots(DepotName.MEDIUM, DepotName.LOW);
        wareHouse.insert(DepotName.FIRST_EXTRA, Resource.SERVANT);
        wareHouse.moveDepots(DepotName.LOW, DepotName.FIRST_EXTRA);

        assertNull(wareHouse.depotByName(DepotName.HIGH).getResource());
        assertEquals(0, wareHouse.depotByName(DepotName.HIGH).getQuantity());

        assertEquals(Resource.COIN, wareHouse.depotByName(DepotName.MEDIUM).getResource());
        assertEquals(2, wareHouse.depotByName(DepotName.MEDIUM).getQuantity());

        assertEquals(Resource.SERVANT, wareHouse.depotByName(DepotName.LOW).getResource());
        assertEquals(1, wareHouse.depotByName(DepotName.LOW).getQuantity());

        assertEquals(Resource.SERVANT, wareHouse.depotByName(DepotName.FIRST_EXTRA).getResource());
        assertEquals(2, wareHouse.depotByName(DepotName.FIRST_EXTRA).getQuantity());
    }
}