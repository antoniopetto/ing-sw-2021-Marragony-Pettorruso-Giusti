package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.playerboard.Resource;
import it.polimi.ingsw.model.shared.Identifiable;

import java.util.HashMap;
import java.util.Map;

/**
 * this class implements the production power. It has a map of resources as input and a map of resources as output, so
 * the player uses the power it checks if it has the resources in the input maps and it receives the resources in the
 * output map
 */
public class ProductionPower {
    private final Map<Resource, Integer> inputResources;
    private final Map<Resource, Integer> outputResources;
    private final int agnosticInput;
    private final int agnosticOutput;

    public ProductionPower(int agnosticInput, int agnosticOutput) {
        this(new HashMap<>(), new HashMap<>(), agnosticInput, agnosticOutput);
    }

    public ProductionPower(Map<Resource, Integer> inputResources, Map<Resource, Integer> outputResources) {
        this(inputResources, outputResources, 0,0);
    }

    public ProductionPower(Map<Resource, Integer> inputResources, Map<Resource, Integer> outputResources, int agnosticInput, int agnosticOutput) {
        this.agnosticInput = agnosticInput;
        this.agnosticOutput = agnosticOutput;
        if(!(inputResources.isEmpty())&&inputResources.get(Resource.FAITH) != 0){
            throw new IllegalArgumentException("Cannot have Faith as input resource");
        }
        this.inputResources = inputResources;
        this.outputResources = outputResources;
    }

    public Map<Resource, Integer> getInputResources() {
        return inputResources;
    }

    public Map<Resource, Integer> getOutputResources() {
        return outputResources;
    }

    public int getAgnosticInput() {
        return agnosticInput;
    }

    public int getAgnosticOutput() {
        return agnosticOutput;
    }

}