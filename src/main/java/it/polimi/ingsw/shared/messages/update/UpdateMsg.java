package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.shared.messages.Msg;
import it.polimi.ingsw.client.simplemodel.SimpleModel;

public interface UpdateMsg extends Msg {

    void execute(SimpleModel game);
}
