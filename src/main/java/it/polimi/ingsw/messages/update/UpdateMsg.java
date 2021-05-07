package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

import java.io.Serializable;

public interface UpdateMsg extends Serializable {

    void execute(SimpleGame model);
}
