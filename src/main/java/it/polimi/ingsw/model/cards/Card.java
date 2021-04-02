package it.polimi.ingsw.model.cards;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card {
    private final int id;
    private final int victoryPoints;

    public Card(int id, int victoryPoints)
    {
        this.id = id;
        this.victoryPoints=victoryPoints;
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

}
