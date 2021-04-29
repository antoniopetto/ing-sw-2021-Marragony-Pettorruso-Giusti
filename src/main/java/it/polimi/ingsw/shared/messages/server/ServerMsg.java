package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

import java.io.Serializable;

public interface ServerMsg extends Serializable {

    void execute(SimpleGame model);
}
