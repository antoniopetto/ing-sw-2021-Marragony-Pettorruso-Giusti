package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.client.simplemodel.SimpleAbility;
import it.polimi.ingsw.client.simplemodel.SimpleCardRequirement;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.shared.Card;
import it.polimi.ingsw.shared.Resource;

import java.util.*;

/**
 * This class represents the leader card. It has three attributes: a list of generic requirements (they can be resource
 * requirements or card requirements); a boolean attribute played which tells if the card has been played and a special
 * ability attribute. The class extends the abstract class card.
 */
public class LeaderCard extends Card {

    private final List<Requirement> requirements = new ArrayList<>();
    private boolean played = false;
    private final SpecialAbility ability;

    public LeaderCard(int id, int victoryPoints, List<? extends Requirement> requirements, SpecialAbility ability) {
        super(id, victoryPoints);
        if(requirements == null || ability == null)
            throw new IllegalArgumentException();
        this.requirements.addAll(requirements);
        this.ability = ability;
    }

    public SimpleLeaderCard getSimple(){

        SimpleAbility ability = this.ability.getSimple();
        Map<Resource, Integer> resReqMap = new HashMap<>();
        List<SimpleCardRequirement> cardReqList = new ArrayList<>();
        for (Requirement req : requirements) {
            if (req instanceof ResourceRequirement)
                resReqMap.put(((ResourceRequirement) req).getResource(), ((ResourceRequirement) req).getQuantity());
            else if (req instanceof CardRequirement)
                cardReqList.add(((CardRequirement) req).getSimple());
        }
        return new SimpleLeaderCard(getId(), getVictoryPoints(), cardReqList, resReqMap, ability, played);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderCard that = (LeaderCard) o;
        return played == that.played && requirements.equals(that.requirements) && ability.equals(that.ability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirements, played, ability);
    }
}
