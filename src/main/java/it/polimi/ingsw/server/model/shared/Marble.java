package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.model.playerboard.Resource;

public enum Marble {
    YELLOW(Resource.COIN),
    BLUE(Resource.SHIELD),
    GREY(Resource.STONE),
    PURPLE(Resource.SERVANT),
    WHITE(),
    RED(Resource.FAITH);

    private Resource resource;

    Marble(){}

    Marble(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}