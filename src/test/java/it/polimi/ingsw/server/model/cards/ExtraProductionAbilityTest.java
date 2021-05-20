package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ExtraProductionAbilityTest {

    @Test
    public void activateAbilityTest() {
        ProductionPower power = new ProductionPower(1,2);
        ExtraProductionAbility ability = new ExtraProductionAbility(power);
        Player player = new Player("Test", Mockito.mock(VirtualView.class));
        ability.activateAbility(player);
        int input = player.getPlayerBoard().getExtraProductionPowers().get(1).getAgnosticInput();
        int output = player.getPlayerBoard().getExtraProductionPowers().get(1).getAgnosticOutput();
        assertEquals(1, input);
        assertEquals(2, output);
    }
}