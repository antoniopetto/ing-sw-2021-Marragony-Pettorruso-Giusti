package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.shared.messages.Msg;
import it.polimi.ingsw.client.simplemodel.SimpleModel;

/**
 * Interface representing all those messages that only interact with a SimpleModel to change the status of a game
 */
public interface UpdateMsg extends Msg {

    void execute(SimpleModel game);
}
