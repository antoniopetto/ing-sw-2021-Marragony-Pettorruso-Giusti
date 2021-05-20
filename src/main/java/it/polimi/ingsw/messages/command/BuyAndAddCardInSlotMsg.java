package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.CardColor;

public class BuyAndAddCardInSlotMsg implements CommandMsg {

    private final CardColor cardColor;
    private final int level;
    private final int slotIdx;

    public BuyAndAddCardInSlotMsg(CardColor cardColor, int level, int slotIdx) {
        this.cardColor = cardColor;
        this.level = level;
        this.slotIdx = slotIdx;
    }

    @Override
    public void execute(Game game) {
        game.buyAndAddCardInSlot(cardColor, level, slotIdx);
    }

    @Override
    public String toString() {
        return "BuyandAddCardInSlotMsg{" +
                "cardColor=" + cardColor +
                ", level=" + level +
                ", slotId=" + slotIdx +
                '}';
    }
}
