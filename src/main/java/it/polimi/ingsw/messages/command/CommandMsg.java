package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.GameController;

import java.io.IOException;
import java.io.Serializable;

public interface CommandMsg extends Serializable {
    void execute(GameController gameController) throws IOException;
}
