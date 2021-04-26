package it.polimi.ingsw.shared.messages.view;

public class ErrorMsg implements ViewMsg {

    private String text;

    public ErrorMsg(String text) {
        this.text = text;
    }

    @Override
    public void changeView() {

    }
}
