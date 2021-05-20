package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

import static org.junit.Assert.*;

public class WhiteMarbleAbilityTest {
    private WhiteMarbleAbility ability;

    @Test
    public void activateAbilityTest()
    {
        ability = new WhiteMarbleAbility(Resource.COIN);
        Player player = new Player("Test", Mockito.mock(VirtualView.class));
        try {
            ability.activateAbility(player);
        }catch (NullPointerException e) {
            assertTrue(true);
        }catch (Exception e) {
            fail();
        }

        assertTrue(player.getWhiteMarbleAliases().contains(Resource.COIN));
    }
}