package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.shared.CardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.shared.ProductionPower;
import it.polimi.ingsw.server.model.cards.ResourceRequirement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
        playerBoard.setVirtualView(Mockito.mock(VirtualView.class));
        resourceRequirementList = new ArrayList<>();
        productionPower = new ProductionPower(0,0);
    }

    @Test
    public void testCanAddCardInSlotEAddCardInSlot(){
        DevelopmentCard developmentCard2 = new DevelopmentCard(1,10,3, CardColor.BLUE,
                resourceRequirementList, productionPower);
        assertFalse(playerBoard.canAddCardInSlot(developmentCard2, 0));
        DevelopmentCard developmentCard1 = new DevelopmentCard(2,10,1, CardColor.BLUE,
                                                                resourceRequirementList, productionPower);
        assertTrue(playerBoard.canAddCardInSlot(developmentCard1, 0));
        playerBoard.addCardInSlot(developmentCard1, 0);

        assertFalse(playerBoard.canAddCardInSlot(developmentCard2,0));
        DevelopmentCard developmentCard3 = new DevelopmentCard(3,10,1, CardColor.BLUE,
                resourceRequirementList, productionPower);
        assertFalse(playerBoard.canAddCardInSlot(developmentCard3,0));
        try {
            playerBoard.addCardInSlot(developmentCard2,0);
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

    }
}