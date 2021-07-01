package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

/**
 * This class is the message of error when something wrong is done and an error message is shown.
 */
public class ErrorMsg implements ToViewMsg {

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
