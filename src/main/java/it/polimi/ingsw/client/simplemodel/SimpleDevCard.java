package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class SimpleDevCard {

    private  int id;
    private  int victoryPoints;
    private  Map<Resource, Integer> requirements;
    private  CardColor color;
    private  int level;
    private  Map<Resource, Integer> input;
    private  Map<Resource, Integer> output;

    private static final SimpleCardParser simpleCardParser = SimpleCardParser.getInstance();

    public static SimpleDevCard parse(int id){
        return simpleCardParser.getSimpleDevelopmentCard(id);
    }

    public SimpleDevCard(int id, int victoryPoints, CardColor color, int level, Map<Resource, Integer> requirements,
                         Map<Resource, Integer> input, Map<Resource, Integer> output) {
        this.id = id;
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.color = color;
        this.level = level;
        this.input = input;
        this.output = output;
    }

    public int getId() {
        return id;
    }

    public Map<Resource, Integer> getRequirements() {
        return requirements;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public void setRequirements(Map<Resource, Integer> requirements) {
        this.requirements = requirements;
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
}
