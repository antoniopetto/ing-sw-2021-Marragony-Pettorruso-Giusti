package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Slot;

import java.util.List;
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

    public CardRequirement(CardColor color, int quantity)
    {
        this.color=color;
        this.quantity = quantity;
        level=Optional.empty();
    }

    /**
     * This method checks if the player has the cards required to satisfy this requirement
     */
    @Override
    public boolean isSatisfied(Player player) {
        int needed = this.quantity;

        List<Slot> slots = player.getPlayerBoard().getSlotList();
        for (Slot slot:slots) {
            List<DevelopmentCard> cards= slot.getDevelopmentCardList();
            for (DevelopmentCard card:cards) {
                if (card.getColor().equals(this.color))
                {
                    if(level.isPresent())
                    {
                        if(level.get()<=card.getLevel())
                            needed--;
                    }
                    else needed--;

                }
                if(needed==0) return true;
            }
        }
        return false;
    }
}
