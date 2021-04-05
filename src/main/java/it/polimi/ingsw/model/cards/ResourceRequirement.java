package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

import java.util.ArrayList;

/**
 * this class implements Requirement and represents a requirement of resources. It has an integer attribute for the
 * quantity of the resource and a Resource attribute for the type of resource.
 */
public class ResourceRequirement implements Requirement{
    private final int quantity;
    private final Resource resource;

    public ResourceRequirement(int quantity, Resource resource) {
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
}
