package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.simplemodel.SimpleAbility;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the extra production power ability. The attribute resource represents the input resource of
 * the production power
 */
public class ExtraProductionAbility implements SpecialAbility, Serializable {
    private final ProductionPower productionPower;

    public ExtraProductionAbility(ProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    @Override
    public void activateAbility(Player player) {
        player.getPlayerBoard().addExtraProductionPower(productionPower);
    }

    @Override
    public SimpleAbility getSimple(){
        // here we suppose that a special production power has only one known input
        Resource resource = productionPower.getInput().keySet().iterator().next();
        return new SimpleAbility(SimpleAbility.Type.EXTRAPRODUCTION, resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraProductionAbility that = (ExtraProductionAbility) o;
        return productionPower.equals(that.productionPower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productionPower);
    }
}