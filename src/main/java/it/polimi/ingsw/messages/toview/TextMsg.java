package it.polimi.ingsw.messages.toview;


import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

public class TextMsg implements ToViewMsg {

    private final String text;
    private final boolean loud;

    public TextMsg(String text) {
        this.text = text;
        loud = false;
    }

    public TextMsg(String text, boolean loud){
        this.text = text;
        this.loud = loud;
    }

    @Override
    public void changeView(View view, ServerHandler server){
        view.showTextMessage(text, loud);
    }

    @Override
    public String toString() {
        return "TextMsg{" +
                "text='" + text + '\'' +
                '}';
    }
}
