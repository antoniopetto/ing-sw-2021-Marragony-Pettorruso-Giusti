package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class SimpleCard {

    private int id;
    private Map<Resource, Integer> requirement;
    private CardColor color;
    private int level;
    private Map<Resource, Integer> input;
    private Map<Resource, Integer> output;
    private int victoryPoints;

    public SimpleCard(int id) {
        this.id = id;
        //TODO parsing
    }

    public int getId() {
        return id;
    }

    public Map<Resource, Integer> getRequirement() {
        return requirement;
    }

    public CardColor getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public Map<Resource, Integer> getInput() {
        return input;
    }

    public Map<Resource, Integer> getOutput() {
        return output;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setRequirement(Map<Resource, Integer> requirement) {
        this.requirement = requirement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setInput(Map<Resource, Integer> input) {
        this.input = input;
    }

    public void setOutput(Map<Resource, Integer> output) {
        this.output = output;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
