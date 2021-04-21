package it.polimi.ingsw.shared.messages;

import it.polimi.ingsw.client.simplemodel.SimpleModel;

public interface ServerMsg {

    void execute(SimpleModel model);
}
