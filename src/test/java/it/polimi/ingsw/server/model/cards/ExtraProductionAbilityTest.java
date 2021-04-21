package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExtraProductionAbilityTest {
    private ExtraProductionAbility ability;

    @Test
    public void activateAbilityTest()
    {
        ProductionPower power = new ProductionPower(1,2);
        ability = new ExtraProductionAbility(power);
        Player player = new Player("Test");
        ability.activateAbility(player);
        int input = player.getPlayerBoard().getExtraProductionPowers().get(0).getAgnosticInput();
        int output = player.getPlayerBoard().getExtraProductionPowers().get(0).getAgnosticOutput();
        assertEquals(1, input);
        assertEquals(2, output);

    }
}