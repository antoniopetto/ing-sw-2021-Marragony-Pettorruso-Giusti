package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class SimpleLeaderCard {
    private enum power {
        DISCOUNT,
        WHITEMARBLE,
        EXTRADEPOT,
        EXTRAPRODUCTION
    }
    private Resource powerResource;
    private int id;
    private int victoryPoints;
    private Map<Map<CardColor, Integer>, Integer> cardRequirement;
    private Map<Resource, Integer> resourceRequirement;
    private boolean active = false;

    public SimpleLeaderCard(int id) {
        this.id=id;
        //parsing from the config file
    }

    public int getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
