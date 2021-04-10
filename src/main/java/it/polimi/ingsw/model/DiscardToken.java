package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardColor;

/**
 * This class represents a discard token. When drawn, two cards of the color attribute are discarded
 */
public class DiscardToken implements SoloActionToken{
    private final CardColor color;

    public DiscardToken(CardColor color) {
        this.color = color;
    }

    @Override
    public void activateToken(Game game) {
        game.getDevelopmentCardDecks().discard(color);
    }
}
