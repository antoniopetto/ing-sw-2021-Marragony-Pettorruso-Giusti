package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.messages.Msg;
import it.polimi.ingsw.client.simplemodel.SimpleModel;

import java.io.IOException;
import java.io.Serializable;

public interface UpdateMsg extends Msg {

    void execute(SimpleModel game);
}
