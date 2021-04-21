package it.polimi.ingsw.server.model.playerboard;

public enum DepotName {
    HIGH (0) ,
    MEDIUM (1),
    LOW (2),
    FIRST_EXTRA (3),
    SECOND_EXTRA (4);

    private final int position;

    DepotName(int position) {
        this.position = position;
    }

    public int getPosition() { return position; }
}
