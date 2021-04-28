package it.polimi.ingsw.shared.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.views.View;

import java.io.IOException;

public class UsernameRequestMsg implements ViewMsg{

    @Override
    public void changeView(View view, ServerHandler handler) throws IOException {
        String username = view.getUsername();
        UsernameMsg message = new UsernameMsg(username);
        handler.writeObject(message);
    }
}
