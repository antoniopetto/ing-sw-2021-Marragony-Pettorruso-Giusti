package it.polimi.ingsw.shared.exceptions;

public class IllegalConfigXMLException extends RuntimeException{

    public IllegalConfigXMLException(String s) {
        super("Malformed XML:" + s);
    }
}
