package it.polimi.ingsw.model.shared;

import it.polimi.ingsw.model.shared.IdGenerator;

public abstract class Identifiable {

    private final int id;

    public Identifiable(){
        id = IdGenerator.getNext();
    }

    public int getId() { return id; }
}
