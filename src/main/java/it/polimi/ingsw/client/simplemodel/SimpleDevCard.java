package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.shared.Card;
import it.polimi.ingsw.shared.CardColor;
import it.polimi.ingsw.shared.CardParser;
import it.polimi.ingsw.shared.Resource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simplified version of DevelopmentCard
 * Requirements are represented by a Map<Resource, Integer>
 */
public class SimpleDevCard extends Card implements Serializable {

    private Map<Resource, Integer> requirements;
    private CardColor color;
    private int level;
    private Map<Resource, Integer> input;
    private Map<Resource, Integer> output;

    private static final List<SimpleDevCard> devCards;

    static{
        try{
            devCards = new ArrayList<>(CardParser.getInstance().parseSimpleDevelopmentCards());
        }
        catch (SAXException | IOException | ParserConfigurationException e){
            e.printStackTrace();
            throw new UncheckedIOException(new IOException("Could not initialize the card parser"));
        }
    }

    /**
     * Static method to get a SimpleDevCard by its id
     * @param id    The card's id
     * @return      The required card
     */
    public static SimpleDevCard parse(int id){
        return Card.getById(id, devCards);
    }

    public SimpleDevCard(int id, int victoryPoints, CardColor color, int level, Map<Resource, Integer> requirements,
                         Map<Resource, Integer> input, Map<Resource, Integer> output) {
        super(id, victoryPoints);
        this.requirements = requirements;
        this.color = color;
        this.level = level;
        this.input = input;
        this.output = output;
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
