package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;

/**
 * This class represents the extra production power ability. The attribute resource represents the input resource of
 * the production power
 */
public class ExtraProductionAbility implements SpecialAbility{
    private final ProductionPower productionPower;

    public ExtraProductionAbility(ProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    @Override
    public void activateAbility(Player player) {

        player.getPlayerBoard().addExtraProductionPower(productionPower);
    }
}