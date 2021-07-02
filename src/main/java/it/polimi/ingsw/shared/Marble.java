package it.polimi.ingsw.shared;

/**
 * Enum - codified types of marble, each linked with its corresponding resource, a part from WHITE
 */
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
