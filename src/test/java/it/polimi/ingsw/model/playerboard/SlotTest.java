package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.ProductionPower;
import it.polimi.ingsw.model.cards.ResourceRequirement;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class SlotTest {
    private Slot slot;
    private List<ResourceRequirement> requirements;
    private ProductionPower productionPower;

    @Before
    public void testInizializeSlot(){
        slot = new Slot(1);
        requirements = new ArrayList<>();
        productionPower = new ProductionPower(0,0);
    }

    @Test
    public void testGetId(){
        assertEquals(1, slot.getId());
    }


    @Test
    public void testGetLastCard(){

        slot.addCard(new DevelopmentCard(0,10,
                1, CardColor.BLUE,requirements,productionPower));
        slot.addCard(new DevelopmentCard(1,15,
                2, CardColor.GREEN,requirements,productionPower));
        DevelopmentCard developmentCard = new DevelopmentCard(1,15,
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
    public void testTryToAddCard(){
        DevelopmentCard developmentCard = new DevelopmentCard(0,10,
                2, CardColor.BLUE,requirements,productionPower);
        assertFalse(slot.tryAddCard(developmentCard));

        DevelopmentCard developmentCard2 = new DevelopmentCard(0,10,
                1, CardColor.BLUE,requirements,productionPower);
        assertTrue(slot.tryAddCard(developmentCard2));

        slot.addCard(developmentCard2);

        DevelopmentCard developmentCard3 = new DevelopmentCard(0,10,
                1, CardColor.BLUE,requirements,productionPower);
        DevelopmentCard developmentCard4 = new DevelopmentCard(0,10,
                3, CardColor.BLUE,requirements,productionPower);

        assertFalse(slot.tryAddCard(developmentCard3));
        assertFalse(slot.tryAddCard(developmentCard4));
        assertTrue(slot.tryAddCard(developmentCard));

        slot.addCard(developmentCard);
        slot.addCard(developmentCard4);
        assertFalse(slot.tryAddCard(developmentCard3));


    }

    @Test
    public void testAddCard(){
        slot.addCard(new DevelopmentCard(1,15,
                1, CardColor.GREEN,requirements,productionPower));
        DevelopmentCard developmentCard1 = new DevelopmentCard(0,10,
                3, CardColor.BLUE,requirements,productionPower);
        try {
            slot.addCard(developmentCard1);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        DevelopmentCard developmentCard = new DevelopmentCard(0,10,
                2, CardColor.BLUE,requirements,productionPower);
        slot.addCard(developmentCard);

        assertEquals(slot.getDevelopmentCardList().get(1),developmentCard);

    }

    @Test
    public void testCountCardPoints(){

        slot.addCard(new DevelopmentCard(0,10,
                1, CardColor.BLUE,requirements,productionPower));
        slot.addCard(new DevelopmentCard(1,15,
                2, CardColor.GREEN,requirements,productionPower));

        assertEquals(25, slot.countCardPoints());

    }



    @Test
    public void testIsEmpty(){

        assertTrue(slot.isEmpty());

        slot.addCard(new DevelopmentCard(0,10,
                1, CardColor.BLUE,requirements,productionPower));

        assertFalse(slot.isEmpty());

    }

}