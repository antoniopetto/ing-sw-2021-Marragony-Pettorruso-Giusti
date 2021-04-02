package it.polimi.ingsw.model.cards;

/**
 * this interface represent a generic requirement, which can be either a card requirement or a resource requirement.
 */
public interface Requirement {
    /**
     * Method that checks if the requirement is satisfied for that player
     * @return true if the requirement is satisfied
     */
    public boolean isSatisfied(); //it has a Player parameter, but the Player class hasn't been created yet
}
