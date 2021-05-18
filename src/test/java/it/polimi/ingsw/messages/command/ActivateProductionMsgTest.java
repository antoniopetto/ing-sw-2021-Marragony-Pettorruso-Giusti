package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ActivateProductionMsgTest {

    @Test
    public void testToString() {
        CommandMsg msg = new ActivateProductionMsg(Set.of(1, 3, 4), Map.of(1, new ProductionPower(1, 1)));
        System.out.println(msg);
    }
}