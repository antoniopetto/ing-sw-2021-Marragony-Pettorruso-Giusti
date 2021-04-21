package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.shared.Identifiable;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card extends Identifiable {

    private final int victoryPoints;

    public Card(int id, int victoryPoints)
    {
        super(id);
        if(victoryPoints < 0) throw  new IllegalArgumentException();
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

    public int getId() {
        return super.getId();
    }
}
