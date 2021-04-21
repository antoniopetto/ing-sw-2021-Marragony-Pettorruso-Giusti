package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.singleplayer.SoloRival;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoloRivalTest {
    private SoloRival soloRival;
    private Game game;

    @Before
    public void setUp()
    {
        soloRival = new SoloRival();
        game = Game.newSinglePlayerGame("username");


    }

    @Test
    public void constructorTest() {
        SoloRival soloRival1 = new SoloRival();
        soloRival.soloTurn(game);
        soloRival1.soloTurn(game);
        assertNotEquals(soloRival.getLastPlayedToken().getId(), soloRival1.getLastPlayedToken().getId());
    }
}