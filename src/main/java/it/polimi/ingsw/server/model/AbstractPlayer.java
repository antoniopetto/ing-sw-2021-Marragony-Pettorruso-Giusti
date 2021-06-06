package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.shared.Position;

import java.io.Serializable;

public abstract class AbstractPlayer implements Serializable {
    private Position position;

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public abstract void vaticanReportEffect(int tileNumber);
    public abstract String getUsername();
}
