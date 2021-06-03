package it.polimi.ingsw.messages.toview;


import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;

public class TextMsg implements ViewMsg {
    private final String text;

    public TextMsg(String text) {
        this.text = text;
    }

    @Override
    public void changeView(View view, ServerHandler server){
        view.showTextMessage(text);
    }

    @Override
    public String toString() {
        return "TextMsg{" +
                "text='" + text + '\'' +
                '}';
    }
}
