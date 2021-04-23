package it.polimi.ingsw.shared.messages;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

public interface ServerMsg {

    void execute(SimpleGame model);
}
