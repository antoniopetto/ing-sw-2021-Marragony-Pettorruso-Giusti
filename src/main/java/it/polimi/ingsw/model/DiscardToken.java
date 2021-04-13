package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardColor;

/**
 * This class represents a discard token. When drawn, two cards of the color attribute are discarded
 */
public class DiscardToken implements SoloActionToken{
    private final CardColor color;
    private final int id;

    public DiscardToken(CardColor color, int id) {
        this.color = color;
        this.id = id;
    }

    @Override
    public void activateToken(Game game) {
        game.getDevelopmentCardDecks().discard(color);
    }

    @Override
    public int getId() {
        return id;
    }
}
