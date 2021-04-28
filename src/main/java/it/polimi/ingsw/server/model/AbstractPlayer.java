package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.shared.Position;

public abstract class AbstractPlayer {
    private Position position;

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public abstract void vaticanReportEffect(int tileNumber);
    public abstract String getUsername();
}
