package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * This class represents the card discount ability. The attribute resource represents the resource of the discount
 */
public class CardDiscountAbility implements SpecialAbility{

    private final Resource resource;
    private final int amount;

    public CardDiscountAbility(Resource resource, int amount)
    {
        this.resource = resource;
        this.amount = amount;
    }

    @Override
    public void activateAbility(Player player) {
        player.addActiveDiscount(resource, amount);
    }
}
