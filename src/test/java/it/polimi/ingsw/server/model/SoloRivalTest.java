package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.singleplayer.SoloRival;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SoloRivalTest {
    private SoloRival soloRival;
    private Game game;

    @Before
    public void setUp() {
        game = new Game(Set.of("username"), Mockito.mock(VirtualView.class));
    }
}