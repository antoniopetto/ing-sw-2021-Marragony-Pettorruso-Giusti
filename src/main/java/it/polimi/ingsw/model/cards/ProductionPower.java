package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.playerboard.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class implements the production power. It has a map of resources as input and a map of resources as output, so
 * the player uses the power it checks if it has the resources in the input maps and it receives the resources in the
 * output map
 */
public class ProductionPower {
    private final List<ResourceRequirement> inputResources;
    private final Map<Resource, Integer> outputResources;

    public ProductionPower(List<ResourceRequirement> inputResources, Map<Resource, Integer> outputResources) {
        this.inputResources = inputResources;
        this.outputResources = outputResources;
    }

    public List<ResourceRequirement> getInputResources() {
        return inputResources;
    }

    public Map<Resource, Integer> getOutputResources() {
        return outputResources;
    }


}

