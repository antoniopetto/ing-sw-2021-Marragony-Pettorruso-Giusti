package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.cards.ResourceRequirement;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerBoardTest {
    private PlayerBoard playerBoard;
    private List<ResourceRequirement> resourceRequirementList;
    private ProductionPower productionPower;

    @Before
    public void testInitPlayerBoard(){
        playerBoard = new PlayerBoard();
        resourceRequirementList = new ArrayList<>();
        productionPower = new ProductionPower(0,0);
    }

    @Test
    public void testCanAddCardInSlotEAddCardInSlot(){
        DevelopmentCard developmentCard2 = new DevelopmentCard(1,10,3, CardColor.BLUE,
                resourceRequirementList, productionPower);
        assertFalse(playerBoard.canAddCardInSlot(developmentCard2, 0));
        DevelopmentCard developmentCard1 = new DevelopmentCard(1,10,1, CardColor.BLUE,
                                                                resourceRequirementList, productionPower);
        assertTrue(playerBoard.canAddCardInSlot(developmentCard1, 0));
        playerBoard.addCardInSlot(developmentCard1, 0);

        assertFalse(playerBoard.canAddCardInSlot(developmentCard2,0));
        DevelopmentCard developmentCard3 = new DevelopmentCard(1,10,1, CardColor.BLUE,
                resourceRequirementList, productionPower);
        assertFalse(playerBoard.canAddCardInSlot(developmentCard3,0));
        try {
            playerBoard.addCardInSlot(developmentCard2,0);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

    }

    @Test
    public void testCountVictoryPoints(){
        DevelopmentCard developmentCard1 = new DevelopmentCard(1,10,1, CardColor.BLUE,
                resourceRequirementList, productionPower);
        playerBoard.addCardInSlot(developmentCard1,0);
        DevelopmentCard developmentCard2 = new DevelopmentCard(1,3,2, CardColor.BLUE,
                resourceRequirementList, productionPower);
        playerBoard.addCardInSlot(developmentCard2,0);

        assertEquals(13, playerBoard.countVictoryPoints());

        playerBoard.getStrongBox().addResource(Resource.COIN, 12);

        assertEquals(25, playerBoard.countVictoryPoints());

        playerBoard.getWareHouse().insert(DepotName.MEDIUM,Resource.COIN);
        playerBoard.getWareHouse().insert(DepotName.MEDIUM,Resource.COIN);

        assertEquals(27, playerBoard.countVictoryPoints());

    }



}