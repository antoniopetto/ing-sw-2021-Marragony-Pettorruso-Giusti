package it.polimi.ingsw.server.model.exceptions;

public class ElementNotFoundException extends RuntimeException{
    String text;

    public String getMessage() {
        return text;
    }

    public ElementNotFoundException(String text)
    {
        this.text= text;
    }
}
