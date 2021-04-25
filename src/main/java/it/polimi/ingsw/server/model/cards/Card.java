package it.polimi.ingsw.server.model.cards;

import java.util.UUID;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card {

    private final int victoryPoints;
    private final int id;

    public Card(int id, int victoryPoints)
    {
        this.id = id;
        if(victoryPoints < 0) throw  new IllegalArgumentException();
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

    public int getId() {
        return id;
    }
}
