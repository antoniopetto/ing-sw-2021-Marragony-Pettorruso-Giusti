package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * This class represents the leader card. It has three attributes: a list of generic requirements (they can be resource
 * requirements or card requirements); a boolean attribute played which tells if the card has been played and a special
 * ability attribute. The class extends the abstract class card.
 */
public class LeaderCard extends Card{
    private final List<Requirement> requirements;
    private boolean played;
    private final SpecialAbility ability;

    public LeaderCard(int id, int victoryPoints, List<Requirement> requirements, SpecialAbility ability) {
        super(id, victoryPoints);
        this.requirements = requirements;
        this.ability = ability;
        played = false;
    }

    /**
     * This method is used when the leader card is played. It activates the special ability of the card
     * @param player is the player who uses the leader card
     */
    public void play(Player player){
        ability.activateAbility(player);
        this.played=true;
    }

    public boolean isPlayed() {
        return played;
    }

    /**
     * This method checks if the player satisfy all the requirements
     * @param player is the player who wants to use the card
     * @return true if all the requirements are satisfied
     */
    public boolean isPlayable(Player player){
        boolean playable = true;
        for (Requirement req:requirements) {
            if(!req.isSatisfied(player))
                playable=false;
        }
        return playable;
    }
}
