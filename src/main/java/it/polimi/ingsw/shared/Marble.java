package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.Resource;

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
