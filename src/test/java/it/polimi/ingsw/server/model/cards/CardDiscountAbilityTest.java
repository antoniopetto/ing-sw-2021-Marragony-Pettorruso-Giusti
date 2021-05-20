package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CardDiscountAbilityTest {
    private CardDiscountAbility ability;

    @Test
    public void constructorTest()
    {
        //checks that it is impossible to create a discount ability with a negative amount
        try {
            ability= new CardDiscountAbility(Resource.SHIELD, -2);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        //checks that it is impossible to create a discount ability with a FAITH resource
        try {
            ability = new CardDiscountAbility(Resource.FAITH, 2);
            fail();
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            ability= new CardDiscountAbility(Resource.COIN, 1);
        }catch (Exception e) {
            fail();
        }
    }

    @Test
    public void activateAbilityTest() {
        Player player = new Player("Test", Mockito.mock(VirtualView.class));
        ability = new CardDiscountAbility(Resource.COIN, 2);
        ability.activateAbility(player);
        assertTrue(player.getActiveDiscount().containsKey(Resource.COIN));
        Integer amount = 2;
        assertEquals(amount, player.getActiveDiscount().get(Resource.COIN));
    }
}