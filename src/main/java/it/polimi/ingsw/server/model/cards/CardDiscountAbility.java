package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.simplemodel.SimpleAbility;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the card discount ability. The attribute resource represents the resource of the discount
 */
public class CardDiscountAbility implements SpecialAbility, Serializable {

    private final Resource resource;

    /**
     * Creates an ability with a discount of <code>amount</code> for a selected <code>resource</code>
     *
     * @param resource      The selected resource
     */
    public CardDiscountAbility(Resource resource) {

        if(resource.equals(Resource.FAITH)) throw new IllegalArgumentException();
        this.resource = resource;
    }

    @Override
    public void activateAbility(Player player) {
        player.addActiveDiscount(resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDiscountAbility that = (CardDiscountAbility) o;
        return resource == that.resource;
    }

    @Override
    public SimpleAbility getSimple(){
        return new SimpleAbility(SimpleAbility.Type.CARDDISCOUNT, resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
}
