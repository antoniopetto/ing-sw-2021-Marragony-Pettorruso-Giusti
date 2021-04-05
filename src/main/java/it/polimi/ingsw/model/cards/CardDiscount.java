package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * This class represents the card discount ability. The attribute resource represents the resource of the discount
 */
public class CardDiscount implements SpecialAbility{

    private final Resource resource;

    public CardDiscount(Resource resource)
    {
        this.resource=resource;
    }

    @Override
    public void activateAbility(Player player) {
        player.setActiveDiscount(this.resource);
    }
}
