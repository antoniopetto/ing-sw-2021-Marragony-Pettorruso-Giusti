package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.shared.CardColor;

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
    public void execute(GameController gameController) {
        gameController.buyAndAddCardInSlot(cardColor, level, slotIdx);
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
