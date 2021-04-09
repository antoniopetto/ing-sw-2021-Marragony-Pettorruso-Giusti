package it.polimi.ingsw.model.playerboard;

public enum DepotName {
    HIGH (1,0) ,
    MEDIUM (2, 1),
    LOW (3, 2),
    FIRST_EXTRA (2, 3),
    SECOND_EXTRA (2, 4);

    private final int capacity;
    private final int position;

    DepotName(int capacity, int position) {
        this.capacity = capacity;
        this.position = position;
    }

    public int getCapacity() { return capacity; }
    public int getPosition() { return position; }
}
