package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

public class ErrorMsg implements ServerMsg {

    private String text;

    public ErrorMsg(String text) {
        this.text = text;
    }

    @Override
    public void execute(SimpleGame model) {

    }
}
