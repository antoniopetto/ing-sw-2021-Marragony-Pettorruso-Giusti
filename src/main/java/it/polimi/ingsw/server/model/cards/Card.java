package it.polimi.ingsw.server.model.cards;

import java.util.UUID;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card {

    private final int victoryPoints;
    private final UUID id = UUID.randomUUID();

    public Card(int victoryPoints)
    {
        if(victoryPoints < 0) throw  new IllegalArgumentException();
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

    public UUID getId() {
        return id;
    }
}
