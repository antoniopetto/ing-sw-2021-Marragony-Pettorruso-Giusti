package it.polimi.ingsw.model.cards;

import java.util.Optional;

/**
 * this class implements Requirement and represents a requirement of cards, which can be present in Leader cards.
 * There are three attributes: color, which represents the CardColor required; level, which is an Optional integer
 * because a level is not always required in the leader card; quantity, an integer which represents the quantity required.
 */
public class CardRequirement implements Requirement{
    private final CardColor color;
    private final Optional<Integer> level;
    private final int quantity;

    public CardRequirement(CardColor color, Integer level, int quantity) {
        this.color = color;
        this.level = Optional.of(level);
        this.quantity = quantity;
    }

    @Override
    public boolean isSatisfied() {
        return false;
    }
}
