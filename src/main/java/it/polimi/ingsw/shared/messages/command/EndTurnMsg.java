package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.GameController;

public class EndTurnMsg implements CommandMsg{
    public void execute(GameController gameController) {
        gameController.endTurn();
    }

    @Override
    public String toString() {
        return "EndTurnMsg{}";
    }
}
