package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.Card;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.CardParser;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a simplified leader card. <code>power</code> represents the power of the card, and
 * <code>powerResource</code> is the resource associated to the power. Each card has an id, the number of victory points
 * and two types of requirements. They can be resource requirements or card requirements, and in this case
 * <code>levelRequired</code> tells if the integer in the map <code>cardRequirement</code> is the level of the card or the
 * number of cards required.
 */
public class SimpleLeaderCard extends Card implements Serializable {

    private final SimpleAbility ability;
    private final Map<Resource, Integer> resourceRequirements;
    private final List<SimpleCardRequirement> cardRequirements;

    private boolean active = false;

    private static final List<SimpleLeaderCard> leaderCards;

    static{
        try{
            leaderCards = new ArrayList<>(CardParser.getInstance().parseSimpleLeaderCards());
        }
        catch (SAXException | IOException | ParserConfigurationException e){
            e.printStackTrace();
            throw new UncheckedIOException(new IOException("Could not initialize the card parser"));
        }
    }

    public static SimpleLeaderCard parse(int id){
        return Card.getById(id, leaderCards);
    }

    public SimpleLeaderCard(int id, int victoryPoints, List<SimpleCardRequirement> cardRequirements,
                            Map<Resource, Integer> resourceRequirements, SimpleAbility ability, boolean active){
        this(id, victoryPoints, cardRequirements, resourceRequirements, ability);
        this.active = active;
    }

    public SimpleLeaderCard(int id, int victoryPoints, List<SimpleCardRequirement> cardRequirements,
                            Map<Resource, Integer> resourceRequirements, SimpleAbility ability){

        super(id, victoryPoints);
        if(cardRequirements == null || resourceRequirements == null || ability == null)
            throw new IllegalArgumentException();

        this.cardRequirements = new ArrayList<>(cardRequirements);
        this.resourceRequirements = new HashMap<>(resourceRequirements);
        this.ability = ability;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public SimpleAbility getAbility() {
        return ability;
    }

    public List<SimpleCardRequirement> getCardRequirements() {
        return cardRequirements;
    }

    public Map<Resource, Integer> getResourceRequirements() {
        return resourceRequirements;
    }
}