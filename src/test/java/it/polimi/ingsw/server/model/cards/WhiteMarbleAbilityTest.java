package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhiteMarbleAbilityTest {
    private WhiteMarbleAbility ability;

    @Test
    public void activateAbilityTest()
    {
        ability = new WhiteMarbleAbility(Resource.COIN);
        Player player = new Player("Test");
        ability.activateAbility(player);
        assertTrue(player.getWhiteMarbleAliases().contains(Resource.COIN));
    }
}