package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;
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