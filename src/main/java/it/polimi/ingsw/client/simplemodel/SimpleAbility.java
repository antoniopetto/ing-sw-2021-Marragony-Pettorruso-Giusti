package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;

/**
 *
 */
public class SimpleAbility implements Serializable {

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
