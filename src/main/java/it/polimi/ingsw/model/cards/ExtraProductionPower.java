package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * This class represents the extra production power ability. The attribute resource represents the input resource of
 * the production power
 */
public class ExtraProductionPower implements SpecialAbility{
    private final Resource resource;

    public ExtraProductionPower(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void activateAbility(Player player) {
        player.setExtraProductionPower(this.resource);
    }
}
