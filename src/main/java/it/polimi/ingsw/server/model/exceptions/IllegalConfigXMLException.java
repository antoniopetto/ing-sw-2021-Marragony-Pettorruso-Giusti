package it.polimi.ingsw.server.model.exceptions;

public class IllegalConfigXMLException extends RuntimeException{

    public IllegalConfigXMLException(String s) {
        super("Malformed XML:" + s);
    }
}
