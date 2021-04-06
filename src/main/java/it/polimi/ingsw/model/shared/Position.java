package it.polimi.ingsw.model.shared;

import java.util.OptionalInt;

public class Position {

    private final int number;
    private final int victoryPoints;
    private final boolean popeSpace;
    private final OptionalInt sectionNumber;

    Position(int number, int victoryPoints, boolean popeSpace){
        this(number, victoryPoints, popeSpace, OptionalInt.empty());
    }

    Position(int number, int victoryPoints, boolean popeSpace, OptionalInt sectionNumber){
        this.number = number;
        this.victoryPoints = victoryPoints;
        this.popeSpace = popeSpace;
        this.sectionNumber = sectionNumber;
    }

    public int getNumber() { return number; }

    public int getVictoryPoints(){ return victoryPoints; }

    public boolean isPopeSpace(){ return popeSpace; }

    public OptionalInt sectionNumber(){ return sectionNumber; }
}
