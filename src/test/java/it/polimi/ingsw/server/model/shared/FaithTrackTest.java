package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class FaithTrackTest {

    FaithTrack faithTrack;
    Player p1, p2;

    @Before
    public void setUp() {
        p1 = new Player("First");
        p2 = new Player("Second");
        //faithTrack = new FaithTrack();
    }

    @Test
    public void vaticanReport() {
    }

    @Test
    public void advance() {
        faithTrack.advance(p1);
    }

    @Test
    public void advanceAllBut() {
    }

    @Test
    public void getLastPosition() {
    }
}