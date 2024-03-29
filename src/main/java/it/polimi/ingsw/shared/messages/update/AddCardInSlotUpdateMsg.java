package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

/**
 * Update message containing the player, the card he's added and the slot where he's added it
 */
public class AddCardInSlotUpdateMsg implements UpdateMsg {

    private final String username;
    private final int cardId;
    private final int slotIdx;


    public AddCardInSlotUpdateMsg(String username, int cardId, int slotIdx) {
        this.username = username;
        this.cardId = cardId;
        this.slotIdx = slotIdx;
    }

    @Override
    public void execute(SimpleModel model) {
        model.update("decks");
        for (SimplePlayer player: model.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.insertCardInSlot(cardId, slotIdx);
        }
    }

    @Override
    public String toString() {
        return "AddCardInSlotUpdateMsg{" +
                "username='" + username + '\'' +
                ", cardId=" + cardId +
                ", slotId=" + slotIdx +
                '}';
    }
}
