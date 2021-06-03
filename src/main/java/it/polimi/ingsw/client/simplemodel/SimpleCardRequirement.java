package it.polimi.ingsw.client.simplemodel;
import it.polimi.ingsw.server.model.cards.CardColor;

import java.io.Serializable;

public class SimpleCardRequirement implements Serializable {

    private final CardColor color;
    private final Integer level;
    private final int quantity;

    public SimpleCardRequirement(CardColor color, int quantity) {
        this(color, null, quantity);
    }

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
