package it.polimi.ingsw.shared.messages;

import it.polimi.ingsw.server.model.Game;

public interface CommandMsg {
    void execute(Game game);
}
