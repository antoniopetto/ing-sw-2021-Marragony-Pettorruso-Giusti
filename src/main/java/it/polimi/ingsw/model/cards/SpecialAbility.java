package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.playerboard.Resource;

/**
 * this interface represents the special ability of a leader card. The type of the ability is chosen at run time
 * using the Strategy pattern
 */
public interface SpecialAbility {

    /**
     *This method activate the ability of the card on the player passed as parameter
     */
    void activateAbility(Player player);
}
