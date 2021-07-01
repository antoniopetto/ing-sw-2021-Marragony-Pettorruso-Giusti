package it.polimi.ingsw.client.simplemodel;
import it.polimi.ingsw.shared.CardColor;

import java.io.Serializable;

/**
 * Simplified version of server.model.cards.CardRequirement
 */
public class SimpleCardRequirement implements Serializable {

    private final CardColor color;
    private final Integer level;
    private final int quantity;

    public SimpleCardRequirement(CardColor color, int quantity) {
        this(color, null, quantity);
    }

    /**
     * Creates a SimpleCardRequirement object
     *
     * @param color     The cards color
     * @param level     The cards level
     * @param quantity  The quantity of cards required
     */
    public SimpleCardRequirement(CardColor color, Integer level, int quantity) {
        if (level != null && (level < 1 || level > 3) || color == null || quantity < 1)
            throw new IllegalArgumentException();
        this.color = color;
        this.level = level;
        this.quantity = quantity;
    }

    public CardColor getColor(){
        return color;
    }

    public Integer getLevel(){
        return level;
    }

    public int getQuantity() {
        return quantity;
    }
}
