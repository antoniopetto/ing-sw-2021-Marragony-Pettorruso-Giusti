package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.singleplayer.SoloRival;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SoloRivalTest {
    private SoloRival soloRival;
    private Game game;

    @Before
    public void setUp()
    {
        Map<String, ClientHandler> players = new HashMap<>();
        players.put("username", null);
        VirtualView virtualView = new VirtualView(players);
        soloRival = new SoloRival();
        game = Game.newSinglePlayerGame("username", virtualView);
    }

    @Test
    public void constructorTest() {
        SoloRival soloRival1 = new SoloRival();
        soloRival.soloTurn(game);
        soloRival1.soloTurn(game);
        assertNotEquals(soloRival.getLastPlayedToken().getId(), soloRival1.getLastPlayedToken().getId());
    }
}