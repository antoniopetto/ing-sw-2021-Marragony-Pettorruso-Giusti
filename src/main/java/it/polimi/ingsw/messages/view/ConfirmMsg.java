package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class ConfirmMsg implements ViewMsg {

    private String text;

    public ConfirmMsg(String text) {
        this.text = text;
    }


    @Override
    public void changeView(View view, ServerHandler server) throws IOException {
        view.showConfirmMessage(text);
    }
}
