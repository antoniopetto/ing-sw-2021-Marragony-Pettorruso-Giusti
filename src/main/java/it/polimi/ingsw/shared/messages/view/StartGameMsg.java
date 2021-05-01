package it.polimi.ingsw.shared.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class StartGameMsg implements ViewMsg{
    @Override
    public void changeView(View view, ServerHandler server) {
        view.startGame();
    }
}
