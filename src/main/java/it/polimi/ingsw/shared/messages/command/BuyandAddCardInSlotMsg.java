package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
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
    public void execute(Game game, ClientHandler handler) throws IOException {
        game.buyandAddCardInSlot(cardColor,level, slotId);
    }
}
