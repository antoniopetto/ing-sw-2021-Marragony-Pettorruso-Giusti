package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;

/**
 * this interface represent a generic requirement, which can be either a card requirement or a resource requirement.
 */
public interface Requirement {
    /**
     * Method that checks if the requirement is satisfied for that player
     * @return true if the requirement is satisfied
     */
    boolean isSatisfied(Player player);
}
