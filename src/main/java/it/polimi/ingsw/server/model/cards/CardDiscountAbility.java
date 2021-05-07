package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Objects;

/**
 * This class represents the card discount ability. The attribute resource represents the resource of the discount
 */
public class CardDiscountAbility implements SpecialAbility{

    private final Resource resource;
    private final int amount;

    /**
     * Creates an ability with a discount of 1 for a selected <code>resource</code>
     *
     *  @param resource         The selected resource
     */
    public CardDiscountAbility(Resource resource){
        this(resource, 1);
    }

    /**
     * Creates an ability with a discount of <code>amount</code> for a selected <code>resource</code>
     *
     * @param resource      The selected resource
     * @param amount        The entity of the discount
     */
    public CardDiscountAbility(Resource resource, int amount) {

        if(amount<1) throw new IllegalArgumentException();
        if(resource.equals(Resource.FAITH)) throw new IllegalArgumentException();
        this.resource = resource;
        this.amount = amount;
    }

    @Override
    public void activateAbility(Player player) {
        player.addActiveDiscount(resource, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDiscountAbility that = (CardDiscountAbility) o;
        return amount == that.amount && resource == that.resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, amount);
    }
}
