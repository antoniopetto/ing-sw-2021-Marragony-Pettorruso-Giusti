package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class FaithTrackTest {

    VirtualView virtualView;
    FaithTrack faithTrack;
    Player p1, p2;

    @Before
    public void setUp() {
        p1 = new Player("First", Mockito.mock(VirtualView.class));
        p2 = new Player("Second", Mockito.mock(VirtualView.class));
        GameController gameController = Mockito.mock(GameController.class);
        virtualView = Mockito.mock(VirtualView.class);
        faithTrack = new FaithTrack(gameController, virtualView);
        faithTrack.addPlayers(List.of(p1, p2));
    }

    @Test
    public void vaticanReport() {
        for (int i = 0; i < 8; i++){
            faithTrack.advance(p1);
        }
        Mockito.verify(virtualView, times(1)).vaticanReportUpdate();
        assertTrue(p1.getTiles().get(0).isGained());
        assertFalse(p2.getTiles().get(0).isGained());
    }

    @Test
    public void advance() {
        faithTrack.advance(p1);
        assertEquals(1, p1.getPosition().getNumber());
    }

    @Test
    public void advanceAllBut() {
        faithTrack.advanceAllBut(p1);
        assertEquals(1, p2.getPosition().getNumber());
        assertEquals(0, p1.getPosition().getNumber());
    }
}