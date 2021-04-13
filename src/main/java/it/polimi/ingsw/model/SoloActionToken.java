package it.polimi.ingsw.model;

/**
 * This interface is used to represent the action tokens in a single player game. There are two types of action tokens,
 * implemented using the Strategy Pattern
 */
public interface SoloActionToken {
    /**
     * This method is used when a token is drawn and activates its ability.
     */
    void activateToken(Game game);

    int getId();
}
