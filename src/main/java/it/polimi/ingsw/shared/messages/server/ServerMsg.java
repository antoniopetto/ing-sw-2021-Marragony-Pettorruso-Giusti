package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

public interface ServerMsg {

    void execute(SimpleGame model);
}
