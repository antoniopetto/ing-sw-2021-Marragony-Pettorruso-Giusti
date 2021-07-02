package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.shared.Marble;

/**
 * It is called when a player decides to discard a contained marble in marbleBuffer.
 * @see CommandMsg
 */
public class DiscardMarbleMsg implements CommandMsg {
    private final Marble marble;

    public DiscardMarbleMsg(Marble marble) {
        this.marble = marble;
    }

    @Override
    public void execute(GameController gameController){
        gameController.discardMarble(marble);
    }

    @Override
    public String toString() {
        return "DiscardMsg{" +
                "marble=" + marble +
                '}';
    }
}
