package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a simplified leader card. <code>power</code> represents the power of the card, and
 * <code>powerResource</code> is the resource associated to the power. Each card has an id, the number of victory points
 * and two types of requirements. They can be resource requirements or card requirements, and in this case
 * <code>levelRequired</code> tells if the integer in the map <code>cardRequirement</code> is the level of the card or the
 * number of cards required.
 */
public class SimpleLeaderCard implements Serializable {

    private final SimpleAbility ability;
    private final int id;
    private final int victoryPoints;
    private final Map<Resource, Integer> resourceRequirements;
    private final List<SimpleCardRequirement> cardRequirements;

    private boolean active = false;

    static final private SimpleCardParser simpleCardParser = SimpleCardParser.getInstance();

    static public SimpleLeaderCard parse(int id) {
        return simpleCardParser.getSimpleLeaderCard(id);
    }

    public SimpleLeaderCard(int id, int victoryPoints, List<SimpleCardRequirement> cardRequirements,
                            Map<Resource, Integer> resourceRequirements, SimpleAbility ability){

        if(cardRequirements == null || resourceRequirements == null || ability == null)
            throw new IllegalArgumentException();

        this.id = id;
        this.victoryPoints = victoryPoints;
        this.cardRequirements = new ArrayList<>(cardRequirements);
        this.resourceRequirements = new HashMap<>(resourceRequirements);
        this.ability = ability;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getId() {
        return id;
    }

    public SimpleAbility getAbility() {
        return ability;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<SimpleCardRequirement> getCardRequirements() {
        return cardRequirements;
    }

    public Map<Resource, Integer> getResourceRequirements() {
        return resourceRequirements;
    }
}