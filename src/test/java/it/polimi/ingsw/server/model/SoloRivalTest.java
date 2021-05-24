package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.singleplayer.SoloRival;
import org.junit.Before;
import org.mockito.Mockito;

import java.util.Set;

public class SoloRivalTest {
    private SoloRival soloRival;
    private GameController gameController;

    @Before
    public void setUp() {
        gameController = new GameController(Set.of("username"), Mockito.mock(VirtualView.class));
    }
}