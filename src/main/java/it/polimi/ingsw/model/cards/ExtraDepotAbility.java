package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * This class represents the extra depot ability, which creates an extra depot in the warehouse. The attribute
 * constraints represents the type of resource that the depot can contain
 */
public class ExtraDepotAbility implements SpecialAbility{

    private final Resource constraint;
    public ExtraDepotAbility(Resource res) {
        constraint=res;
    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerBoard().getWareHouse().createExtraDepot(this.constraint);
    }
}
