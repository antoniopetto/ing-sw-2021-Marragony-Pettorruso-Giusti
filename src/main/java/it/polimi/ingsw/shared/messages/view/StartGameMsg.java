package it.polimi.ingsw.shared.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.views.View;

import java.io.IOException;

public class StartGameMsg implements ViewMsg{
    @Override
    public void changeView(View view, ServerHandler server) {
        view.startGame();
    }
}
