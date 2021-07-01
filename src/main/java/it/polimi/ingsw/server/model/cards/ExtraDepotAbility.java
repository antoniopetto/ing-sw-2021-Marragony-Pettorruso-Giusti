package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.simplemodel.SimpleAbility;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.shared.Resource;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the extra depot ability, which creates an extra depot in the warehouse. The attribute
 * constraints represents the type of resource that the depot can contain
 */
public class ExtraDepotAbility implements SpecialAbility, Serializable {

    private final Resource constraint;
    private final int capacity = 2;

    public ExtraDepotAbility(Resource constraint){
        this.constraint = constraint;
    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerBoard().getWareHouse().createExtraDepot(constraint, capacity);
    }

    public SimpleAbility getSimple(){
        return new SimpleAbility(SimpleAbility.Type.EXTRADEPOT, constraint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraDepotAbility that = (ExtraDepotAbility) o;
        return constraint == that.constraint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, capacity);
    }
}
