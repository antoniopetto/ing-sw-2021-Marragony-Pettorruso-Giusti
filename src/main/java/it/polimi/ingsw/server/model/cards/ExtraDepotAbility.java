package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

/**
 * This class represents the extra depot ability, which creates an extra depot in the warehouse. The attribute
 * constraints represents the type of resource that the depot can contain
 */
public class ExtraDepotAbility implements SpecialAbility{

    private final Resource constraint;
    private final int capacity;

    public ExtraDepotAbility(Resource constraint, int capacity) {
        if (capacity<1) throw new IllegalArgumentException("Illegal capacity");
        this.constraint = constraint;
        this.capacity = capacity;

    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerBoard().getWareHouse().createExtraDepot(constraint, capacity);
    }
}
