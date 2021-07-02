package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;


/**
 * It is called when a player ends his turn.
 * @see CommandMsg
 */
public class EndTurnMsg implements CommandMsg{
    public void execute(GameController gameController) {
        gameController.endTurn();
    }

    @Override
    public String toString() {
        return "EndTurnMsg{}";
    }
}
