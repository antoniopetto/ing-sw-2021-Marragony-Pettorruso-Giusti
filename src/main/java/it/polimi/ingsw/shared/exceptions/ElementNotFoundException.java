package it.polimi.ingsw.shared.exceptions;

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