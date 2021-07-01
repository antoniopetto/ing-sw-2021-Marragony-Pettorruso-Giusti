package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.toview.ToViewMsg;

public class InitModelMsg implements ToViewMsg {

    private final SimpleModel game;

    public InitModelMsg(SimpleModel game){
        this.game = game;
    }

    @Override
    public void changeView(View view, ServerHandler serverHandler) {
        view.setModel(game);
    }

    @Override
    public String toString() {
        return "InitModelMsg{" +
                "game=" + game +
                '}';
    }
}
