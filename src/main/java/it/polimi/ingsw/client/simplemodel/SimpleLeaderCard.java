package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

/**
 * This class represents a simplified leader card. <code>power</code> represents the power of the card, and
 * <code>powerResource</code> is the resource associated to the power. Each card has an id, the number of victory points
 * and two types of requirements. They can be resource requirements or card requirements, and in this case
 * <code>levelRequired</code> tells if the integer in the map <code>cardRequirement</code> is the level of the card or the
 * number of cards required.
 */
public class SimpleLeaderCard {
    public enum Power {
        DISCOUNT,
        WHITEMARBLE,
        EXTRADEPOT,
        EXTRAPRODUCTION
    }
    private Resource powerResource;
    private int id;
    private int victoryPoints;
    private Map<CardColor, Integer> cardRequirement;
    private boolean levelRequired;
    private Power power;

    private Map<Resource, Integer> resourceRequirement;
    private boolean active = false;


    public SimpleLeaderCard(int id) {
        this.id=id;
        //parsing from the config file
    }

    public void setPowerResource(Resource powerResource) {
        this.powerResource = powerResource;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public void setCardRequirement(Map<CardColor, Integer> cardRequirement) {
        this.cardRequirement = cardRequirement;
    }

    public void setLevelRequired(boolean levelRequired) {
        this.levelRequired = levelRequired;
    }

    public void setPower(Power power) {
        this.power = power;
    }

    public void setResourceRequirement(Map<Resource, Integer> resourceRequirement) {
        this.resourceRequirement = resourceRequirement;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Resource getPowerResource() {
        return powerResource;
    }

    public int getId() {
        return id;
    }

    public Power getPower() {
        return power;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Map<CardColor, Integer> getCardRequirement() {
        return cardRequirement;
    }

    public Map<Resource, Integer> getResourceRequirement() {
        return resourceRequirement;
    }

    public boolean isLevelRequired() {
        return levelRequired;
    }
}
