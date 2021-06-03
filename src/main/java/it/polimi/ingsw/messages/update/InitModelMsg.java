package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.toview.ViewMsg;

public class InitModelMsg implements ViewMsg {

    private final SimpleModel game;

    public InitModelMsg(SimpleModel game){
        this.game = game;
    }

    @Override
    public void changeView(View view, ServerHandler serverHandler) {
        view.setModel(game);
    }
}
