package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.shared.*;
import it.polimi.ingsw.server.model.playerboard.PlayerBoard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private PlayerBoard playerBoard;
    private ClientHandler clientHandler;

    @Before
    public void setUp(){

        clientHandler = Mockito.mock(ClientHandler.class);
        VirtualView virtualView = new VirtualView(Map.of("PlayerOne", clientHandler));
        player = virtualView.getGameController().getPlayers().get(0);
        playerBoard = player.getPlayerBoard();
    }

    @Test
    public void activateNormalProductionTest() {

        playerBoard.getWareHouse().insert(DepotName.HIGH, Resource.COIN);
        assertSame(playerBoard.getWareHouse().getDepots().get(0).getResource(), Resource.COIN);
        assertEquals(1, playerBoard.getWareHouse().getDepots().get(0).getQuantity());

        ProductionPower power = new ProductionPower(Map.of(Resource.COIN, 1), Map.of(Resource.FAITH, 1, Resource.SERVANT, 1));
        DevelopmentCard devCard = new DevelopmentCard(1, 10, 1, CardColor.BLUE, new ArrayList<>(), power);
        playerBoard.addCardInSlot(devCard, 0);
        int steps = player.activateProduction(Set.of(1), Map.of());
        assertTrue(playerBoard.getWareHouse().getDepots().get(0).isEmpty());
        assertNull(playerBoard.getWareHouse().getDepots().get(0).getResource());
        assertEquals(1, playerBoard.getStrongBox().getQuantity(Resource.SERVANT));
        assertEquals(1, steps);
    }

    @Test
    public void activateExtraProductionTest() {

        playerBoard.getWareHouse().insert(DepotName.HIGH, Resource.COIN);
        playerBoard.getWareHouse().insert(DepotName.LOW, Resource.STONE);

        ProductionPower power = new ProductionPower(Map.of(Resource.COIN, 1, Resource.STONE, 1), Map.of(Resource.SERVANT, 1));
        int steps = player.activateProduction(Set.of(), Map.of(0, power));
        assertTrue(playerBoard.getWareHouse().getDepots().get(0).isEmpty());
        assertNull(playerBoard.getWareHouse().getDepots().get(0).getResource());
        assertTrue(playerBoard.getWareHouse().getDepots().get(2).isEmpty());
        assertNull(playerBoard.getWareHouse().getDepots().get(2).getResource());
        assertEquals(1, playerBoard.getStrongBox().getQuantity(Resource.SERVANT));
        assertEquals(0, steps);
    }

    @Test
    public void activateComplexProductionTest(){

        // card 15 production power:
        //    input = SHIELD 1, STONE 1
        //    output = SERVANT 2, FAITH 2

        try {
            List<DevelopmentCard> devCards = CardParser.getInstance().parseDevelopmentCards();
            playerBoard.getSlotList().get(0).addCard(Card.getById(15, devCards));
            playerBoard.getWareHouse().insert(DepotName.HIGH, Resource.SHIELD);
            playerBoard.getWareHouse().insert(DepotName.MEDIUM, Resource.STONE);
            playerBoard.getStrongBox().addResource(Resource.STONE, 2);
            ProductionPower specialPower = new ProductionPower(Map.of(Resource.STONE, 2), Map.of(Resource.COIN, 1));
            player.activateProduction(Set.of(15), Map.of(0, specialPower));
            assertEquals(1, playerBoard.getStrongBox().getQuantity(Resource.COIN));
            assertEquals(2, playerBoard.getStrongBox().getQuantity(Resource.SERVANT));
            playerBoard.getWareHouse().insert(DepotName.HIGH, Resource.SHIELD);
            playerBoard.getWareHouse().insert(DepotName.MEDIUM, Resource.STONE);
            player.activateProduction(Set.of(15), Map.of());
            assertEquals(1, playerBoard.getStrongBox().getQuantity(Resource.COIN));
            assertEquals(4, playerBoard.getStrongBox().getQuantity(Resource.SERVANT));
            specialPower = new ProductionPower(Map.of(Resource.SERVANT, 2), Map.of(Resource.COIN, 1));
            player.activateProduction(Set.of(), Map.of(0, specialPower));
            assertEquals(2, playerBoard.getStrongBox().getQuantity(Resource.COIN));
            assertEquals(2, playerBoard.getStrongBox().getQuantity(Resource.SERVANT));
        } catch (Exception e){
            fail();
        }
    }
}