package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Objects;

/**
 * this class implements Requirement and represents a requirement of resources. It has an integer attribute for the
 * quantity of the resource and a Resource attribute for the type of resource.
 */
public class ResourceRequirement implements Requirement{
    private final int quantity;
    private final Resource resource;

    public ResourceRequirement(Resource resource, int quantity) {
        if(quantity<1) throw new IllegalArgumentException("Invalid quantity");
        if(resource.equals(Resource.FAITH)) throw new IllegalArgumentException("Faith is not a resource requirement");
        this.quantity = quantity;
        this.resource = resource;
    }

    /**
     * This method checks if the player has enough resources to satisfy this requirement.
     */
    @Override
    public boolean isSatisfied(Player player) {
        return player.getPlayerBoard().isAffordable(this);

    }

    public int getQuantity() {
        return quantity;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceRequirement that = (ResourceRequirement) o;
        return quantity == that.quantity && resource == that.resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, resource);
    }
}
