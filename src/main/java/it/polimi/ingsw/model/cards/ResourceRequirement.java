package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.playerboard.Resource;

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

    @Override
    public boolean isSatisfied() { //it will be completed after the creation of Player class
        return false;
    }
}
