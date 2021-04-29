package it.polimi.ingsw.shared.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.views.View;

public class ErrorMsg implements ViewMsg {

    private String text;

    public ErrorMsg(String text) {
        this.text = text;
    }


    @Override
    public void changeView(View view, ServerHandler handler) {
        view.showMessage(text);
    }
}
