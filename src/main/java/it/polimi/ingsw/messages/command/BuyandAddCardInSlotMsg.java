package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.CardColor;

import java.io.IOException;

public class BuyandAddCardInSlotMsg implements CommandMsg {

    private CardColor cardColor;
    private int level;
    private int slotId;

    public BuyandAddCardInSlotMsg(CardColor cardColor, int level, int slotId) {
        this.cardColor = cardColor;
        this.level = level;
        this.slotId = slotId;
    }

    @Override
    public void execute(Game game) {
        game.buyAndAddCardInSlot(cardColor,level, slotId);
    }

    @Override
    public String toString() {
        return "BuyandAddCardInSlotMsg{" +
                "cardColor=" + cardColor +
                ", level=" + level +
                ", slotId=" + slotId +
                '}';
    }
}
