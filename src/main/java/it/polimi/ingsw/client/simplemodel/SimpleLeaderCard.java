package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a simplified leader card. <code>power</code> represents the power of the card, and
 * <code>powerResource</code> is the resource associated to the power. Each card has an id, the number of victory points
 * and two types of requirements. They can be resource requirements or card requirements, and in this case
 * <code>levelRequired</code> tells if the integer in the map <code>cardRequirement</code> is the level of the card or the
 * number of cards required.
 */
public class SimpleLeaderCard implements Serializable {
    public enum Ability {
        CARDDISCOUNT,
        WHITEMARBLE,
        EXTRADEPOT,
        EXTRAPRODUCTION
    }

    private final Ability ability;
    private final Resource abilityResource;
    private final int id;
    private final int victoryPoints;
    private final Map<Resource, Integer> resourceRequirements;
    private final Map<CardColor, Map<Integer, Integer>> cardRequirements;

    private boolean active = false;

    static final private SimpleCardParser simpleCardParser = SimpleCardParser.getInstance();

    static public SimpleLeaderCard parse(int id) {
        return simpleCardParser.getSimpleLeaderCard(id);
    }

    public SimpleLeaderCard(int id, int victoryPoints, Map<CardColor, Map<Integer, Integer>> cardRequirements,
                            Map<Resource, Integer> resourceRequirements, Ability ability, Resource abilityResource){

        if(cardRequirements == null || resourceRequirements == null || ability == null || abilityResource == null)
            throw new IllegalArgumentException();

        this.id = id;
        this.victoryPoints = victoryPoints;
        this.cardRequirements = new HashMap<>(cardRequirements);
        this.resourceRequirements = new HashMap<>(resourceRequirements);
        this.ability = ability;
        this.abilityResource = abilityResource;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Resource getAbilityResource() {
        return abilityResource;
    }

    public int getId() {
        return id;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Map<CardColor, Map<Integer, Integer>> getCardRequirements() {
        return cardRequirements;
    }

    public Map<Resource, Integer> getResourceRequirements() {
        return resourceRequirements;
    }
}