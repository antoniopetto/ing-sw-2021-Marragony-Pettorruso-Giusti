package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * this class implements the production power. It has a map of resources as input and a map of resources as output, so
 * the player uses the power it checks if it has the resources in the input maps and it receives the resources in the
 * output map
 */
public class ProductionPower implements Serializable {
    private final Map<Resource, Integer> input;
    private final Map<Resource, Integer> output;
    private final int agnosticInput;
    private final int agnosticOutput;

    public ProductionPower(Integer agnosticInput, Integer agnosticOutput) {
        this(new HashMap<>(), new HashMap<>(), agnosticInput, agnosticOutput);
    }

    public ProductionPower(Map<Resource, Integer> input, Map<Resource, Integer> output) {
        this(input, output, 0,0);
    }

    public ProductionPower(Map<Resource, Integer> input, Map<Resource, Integer> output, Integer agnosticInput, Integer agnosticOutput) {

        this.agnosticInput = agnosticInput != null ? agnosticInput : 0;
        this.agnosticOutput = agnosticOutput != null ? agnosticOutput : 0;
        if (this.agnosticInput < 0 || this.agnosticOutput < 0)
            throw new IllegalArgumentException();

        if (input == null)
            input = new HashMap<>();
        if (output == null)
            output = new HashMap<>();

        if (Stream.concat(input.values().stream(), output.values().stream()).anyMatch(i -> i < 0))
            throw new IllegalArgumentException("Cannot have negative resource quantity");
        if (input.containsKey(Resource.FAITH) && input.get(Resource.FAITH) != 0)
            throw new IllegalArgumentException("Cannot have Faith as input resource");

        this.input = input;
        this.output = output;
    }

    public Map<Resource, Integer> getInput() {
        return input;
    }

    public Map<Resource, Integer> getOutput() {
        return output;
    }

    public int getAgnosticInput() {
        return agnosticInput;
    }

    public int getAgnosticOutput() {
        return agnosticOutput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionPower that = (ProductionPower) o;
        return agnosticInput == that.agnosticInput && agnosticOutput == that.agnosticOutput && input.equals(that.input) && output.equals(that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, output, agnosticInput, agnosticOutput);
    }
}