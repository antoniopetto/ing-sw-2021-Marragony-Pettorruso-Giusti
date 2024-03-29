package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.SpecialAbility;
import it.polimi.ingsw.shared.Resource;

import java.io.Serializable;

/**
 * Simplified version of SpecialAbility
 */
public class SimpleAbility implements Serializable {

    /**
     * Enum - codified types of special ability
     */
    public enum Type {
        CARDDISCOUNT,
        WHITEMARBLE,
        EXTRADEPOT,
        EXTRAPRODUCTION
    }

    private final Type type;
    private final Resource resource;

    public SimpleAbility(Type type, Resource resource) {
        this.type = type;
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public Type getType() {
        return type;
    }
}
