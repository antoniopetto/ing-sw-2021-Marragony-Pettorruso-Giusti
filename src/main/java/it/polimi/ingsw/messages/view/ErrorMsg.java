package it.polimi.ingsw.messages.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class ErrorMsg implements ViewMsg {

    private String text;

    public ErrorMsg(String text) {
        this.text = text;
    }

    @Override
    public void changeView(View view, ServerHandler handler) {
        view.showErrorMessage(text);
    }

    @Override
    public String toString() {
        return "ErrorMsg{" +
                "text='" + text + '\'' +
                '}';
    }
}
