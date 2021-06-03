package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.PlayerBoard;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.FaithTrack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private PlayerBoard playerBoard;

    @Before
    public void setUp(){
        player = new Player("AAA", Mockito.mock(VirtualView.class));
        FaithTrack faithTrack = new FaithTrack(Mockito.mock(VirtualView.class));
        faithTrack.addPlayers(List.of(player));
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
}