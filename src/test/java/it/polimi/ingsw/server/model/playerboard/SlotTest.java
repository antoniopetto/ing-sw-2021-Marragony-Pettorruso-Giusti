package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.shared.CardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.shared.ProductionPower;
import it.polimi.ingsw.server.model.cards.ResourceRequirement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class SlotTest {
    private Slot slot;
    private List<ResourceRequirement> requirements;
    private ProductionPower productionPower;

    @Before
    public void inizializeSlot(){
        slot = new Slot();
        requirements = new ArrayList<>();
        productionPower = new ProductionPower(0,0);
    }

    @After
    public void clearSlot(){
        slot = null;
        productionPower = null;
        requirements.clear();
    }

    @Test
    public void testGetLastCard(){

        slot.addCard(new DevelopmentCard(1,10,
                1, CardColor.BLUE,requirements,productionPower));
        slot.addCard(new DevelopmentCard(2,15,
                2, CardColor.GREEN,requirements,productionPower));
        DevelopmentCard developmentCard = new DevelopmentCard(3,15,
                3, CardColor.YELLOW,requirements,productionPower);
        slot.addCard(developmentCard);

        assertEquals(slot.getLastCard(), developmentCard);
    }

    /*
    @Test
    public void testGetDevelopmentCardList(){
        slot.addCard(new DevelopmentCard(0,10,
                2, CardColor.BLUE,requirements,productionPower));
        developmentCardList.add(new DevelopmentCard(0,10,
                2, CardColor.BLUE,requirements,productionPower));
        slot.addCard(new DevelopmentCard(1,15,
                2, CardColor.GREEN,requirements,productionPower));


        assertEquals(slot.getDevelopmentCardList(),developmentCardList.toArray());
    }
    */

    @Test
    public void testCanAddCard(){
        DevelopmentCard developmentCard = new DevelopmentCard(4,10,
                2, CardColor.BLUE,requirements,productionPower);
        assertFalse(slot.canAddCard(developmentCard));

        DevelopmentCard developmentCard2 = new DevelopmentCard(5,10,
                1, CardColor.BLUE,requirements,productionPower);
        assertTrue(slot.canAddCard(developmentCard2));

        slot.addCard(developmentCard2);

        DevelopmentCard developmentCard3 = new DevelopmentCard(6,10,
                1, CardColor.BLUE,requirements,productionPower);
        DevelopmentCard developmentCard4 = new DevelopmentCard(7,10,
                3, CardColor.BLUE,requirements,productionPower);

        assertFalse(slot.canAddCard(developmentCard3));
        assertFalse(slot.canAddCard(developmentCard4));
        assertTrue(slot.canAddCard(developmentCard));

        slot.addCard(developmentCard);
        slot.addCard(developmentCard4);
        assertFalse(slot.canAddCard(developmentCard3));


    }

    @Test
    public void testAddCard(){
        slot.addCard(new DevelopmentCard(8,15,
                1, CardColor.GREEN,requirements,productionPower));
        DevelopmentCard developmentCard1 = new DevelopmentCard(9,10,
                3, CardColor.BLUE,requirements,productionPower);
        try {
            slot.addCard(developmentCard1);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        DevelopmentCard developmentCard = new DevelopmentCard(10,10,
                2, CardColor.BLUE,requirements,productionPower);
        slot.addCard(developmentCard);

        assertEquals(slot.getDevelopmentCardList().get(1),developmentCard);

    }

    @Test
    public void testCountCardPoints(){

        slot.addCard(new DevelopmentCard(11,10,
                1, CardColor.BLUE,requirements,productionPower));
        slot.addCard(new DevelopmentCard(12,15,
                2, CardColor.GREEN,requirements,productionPower));

        assertEquals(25, slot.countCardPoints());

    }



    @Test
    public void testIsEmpty(){

        assertTrue(slot.isEmpty());

        slot.addCard(new DevelopmentCard(13,10,
                1, CardColor.BLUE,requirements,productionPower));

        assertFalse(slot.isEmpty());

    }

}