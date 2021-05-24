package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.GameController;
import it.polimi.ingsw.server.model.shared.Marble;

public class DiscardMarbleMsg implements CommandMsg {
    private Marble marble;

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
