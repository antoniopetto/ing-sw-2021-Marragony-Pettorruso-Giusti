package it.polimi.ingsw.model.shared;

public abstract class Identifiable {

    private final int id;

    public Identifiable(int id){
        this.id = id;
    }

    public int getId() { return id; }
}
