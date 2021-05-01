package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CardParserTest {

    private CardParser parser;

    @Before
    public void setup(){
        try {
            parser = new CardParser("resources/config.xml");
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            fail();
        }
    }

    @Test
    public void parseDevelopmentCards() {
        List<DevelopmentCard> devCards = parser.parseDevelopmentCards();
        DevelopmentCard devCard34 = Card.getById(34, devCards);

        assertEquals(CardColor.PURPLE, devCard34.getColor());
        assertEquals(devCard34.getRequirements(), List.of(new ResourceRequirement(Resource.SERVANT, 6)));
        assertEquals(3, devCard34.getLevel());
        assertEquals(9, devCard34.getVictoryPoints());
        assertEquals(0, devCard34.getPower().getAgnosticInput());
        assertEquals(0,devCard34.getPower().getAgnosticOutput());
        assertEquals(new ProductionPower(Map.of(Resource.STONE, 2), Map.of(Resource.COIN, 3, Resource.FAITH, 2)),
                     devCard34.getPower());
    }

    @Test
    public void parseLeaderCards() {
        List<LeaderCard> leaderCards = parser.parseLeaderCards();
        LeaderCard leaderCard109 = Card.getById(109, leaderCards);

        List<Requirement> reqs = List.of(new CardRequirement(CardColor.YELLOW, 2), new CardRequirement(CardColor.BLUE, 1));
        assertEquals(new LeaderCard(109, 5, reqs, new WhiteMarbleAbility(Resource.SERVANT)), leaderCard109);
    }

    @Test
    public void parseBaseProductionPower() {
        ProductionPower expectedBasePower = new ProductionPower(2, 1);
        ProductionPower basePower = parser.parseBaseProductionPower();
        assertEquals(expectedBasePower, basePower);
    }
}