package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.shared.messages.Msg;
import it.polimi.ingsw.server.GameController;

import java.io.IOException;

public interface CommandMsg extends Msg {
    void execute(GameController gameController) throws IOException;
}
