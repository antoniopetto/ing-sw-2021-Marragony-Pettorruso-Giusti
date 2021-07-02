package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;

import java.io.Serializable;

/**
 * Update message that sets active SimplePlayer's tile
 */
public class TileGainedUpdateMsg implements UpdateMsg, Serializable {

    private final String username;
    private final int tileIdx;

    public TileGainedUpdateMsg(String username, int tileIdx) {
        this.username = username;
        this.tileIdx = tileIdx;
    }

    @Override
    public void execute(SimpleModel game) {
        game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username)).findAny()
                .ifPresent(p -> p.gainTile(tileIdx));
    }
}
