package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Slot;

import java.util.List;
import java.util.Optional;

/**
 * this class implements Requirement and represents a requirement of cards, which can be present in Leader cards.
 * There are three attributes: color, which represents the CardColor required; level, which is an Optional integer
 * because a level is not always required in the leader card; quantity, an integer which represents the quantity required.
 */
public class CardRequirement implements Requirement{
    private final CardColor color;
    private final Integer level;
    private final int quantity;

    public CardRequirement(CardColor color, int quantity) {
        this(color, null, quantity);
    }

    public CardRequirement(CardColor color, Integer level, int quantity) {
        if (level != null && (level < 1 || level > 3) || color == null || quantity < 1)
            throw new IllegalArgumentException();
        this.color = color;
        this.level = level;
        this.quantity = quantity;
    }

    /**
     * This method checks if the player has the cards required to satisfy this requirement
     */
    @Override
    public boolean isSatisfied(Player player) {
        int needed = this.quantity;
        for (Slot slot : player.getPlayerBoard().getSlotList()) {
            for (DevelopmentCard card : slot.getDevelopmentCardList()) {
                if (card.getColor().equals(this.color)) {
                    if (this.level != null) {
                        if (this.level.equals(card.getLevel()))
                            needed--;
                    }
                    else
                        needed--;
                }
                if (needed == 0)
                    return true;
            }
        }
        return false;
    }
}