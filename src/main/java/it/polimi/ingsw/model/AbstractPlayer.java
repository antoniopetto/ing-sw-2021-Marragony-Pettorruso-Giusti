package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shared.Position;

public abstract class AbstractPlayer {
    private Position position;

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }
    abstract public void vaticanReportEffect(int tileNumber);
}
