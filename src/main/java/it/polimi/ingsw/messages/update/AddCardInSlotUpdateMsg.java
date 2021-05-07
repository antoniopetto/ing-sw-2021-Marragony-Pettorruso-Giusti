package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

public class AddCardInSlotUpdateMsg implements UpdateMsg {

    private String username;
    private int cardId;
    private int slotId;


    public AddCardInSlotUpdateMsg(String username, int cardId, int slotId) {
        this.username = username;
        this.cardId = cardId;
        this.slotId = slotId;
    }

    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer player: model.getPlayers()) {
            if(player.getUsername().equals(this.username))
                player.insertCardInSlot(cardId, slotId);
        }
    }
}
