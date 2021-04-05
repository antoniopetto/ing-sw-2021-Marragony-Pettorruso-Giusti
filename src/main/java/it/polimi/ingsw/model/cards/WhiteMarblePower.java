package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * This class represents the white marble power ability. The attribute resource represents the value of the white
 * marble when bought in the market.
 */
public class WhiteMarblePower implements SpecialAbility{
    private Resource resource;
    public WhiteMarblePower(Resource res)
    {
        this.resource=res;
    }
    @Override
    public void activateAbility(Player player) {
        player.setWhiteMarbleAliases(this.resource);
    }
}
