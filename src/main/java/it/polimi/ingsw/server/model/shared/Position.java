package it.polimi.ingsw.server.model.shared;

import java.io.Serializable;

/**
 * Representation of a cell in the FaithTrack.
 * It contains it number, the associated victory points, if it is a pope space, and the vatican report section number it's in
 * (-1 if it's not in a section)
 */
public class Position implements Serializable {

    private final int number;
    private final int victoryPoints;
    private final boolean popeSpace;
    private final int sectionNumber;

    Position(int number, int victoryPoints, boolean popeSpace, int sectionNumber){
        this.number = number;
        this.victoryPoints = victoryPoints;
        this.popeSpace = popeSpace;
        this.sectionNumber = sectionNumber;
    }

    public int getNumber() { return number; }

    public int getVictoryPoints(){ return victoryPoints; }

    public boolean isPopeSpace(){ return popeSpace; }

    public int sectionNumber(){ return sectionNumber; }
}
