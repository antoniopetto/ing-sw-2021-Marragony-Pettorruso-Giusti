package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Objects;

/**
 * This class represents the extra depot ability, which creates an extra depot in the warehouse. The attribute
 * constraints represents the type of resource that the depot can contain
 */
public class ExtraDepotAbility implements SpecialAbility{

    private final Resource constraint;
    private final int capacity;

    public ExtraDepotAbility(Resource constraint){
        this(constraint, 2);
    }

    public ExtraDepotAbility(Resource constraint, int capacity) {
        if (capacity<1) throw new IllegalArgumentException("Illegal capacity");
        this.constraint = constraint;
        this.capacity = capacity;
    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerBoard().getWareHouse().createExtraDepot(constraint, capacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraDepotAbility that = (ExtraDepotAbility) o;
        return capacity == that.capacity && constraint == that.constraint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, capacity);
    }
}
