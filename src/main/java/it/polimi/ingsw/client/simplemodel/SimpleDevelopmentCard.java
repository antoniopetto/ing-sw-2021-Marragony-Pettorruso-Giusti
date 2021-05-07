package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class SimpleDevelopmentCard {

    private final int id;
    private final int victoryPoints;
    private final Map<Resource, Integer> requirements;
    private final CardColor color;
    private final int level;
    private final Map<Resource, Integer> input;
    private final Map<Resource, Integer> output;

    private static final SimpleCardParser simpleCardParser = SimpleCardParser.getInstance();

    public static SimpleDevelopmentCard parse(int id){
        return simpleCardParser.getSimpleDevelopmentCard(id);
    }

    public SimpleDevelopmentCard(int id, int victoryPoints, CardColor color, int level, Map<Resource, Integer> requirements,
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

}
