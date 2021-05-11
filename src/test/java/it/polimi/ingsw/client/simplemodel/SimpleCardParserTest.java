package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleCardParserTest {

    @Test
    public void constructorTest(){
        SimpleCardParser parser = SimpleCardParser.getInstance();
    }

    @Test
    public void getDevCardTest(){
        SimpleDevelopmentCard devCard = SimpleDevelopmentCard.parse(47);
    }

    @Test
    public void getLeaderCardTest(){
        SimpleLeaderCard leaderCard = SimpleLeaderCard.parse(108);
        System.out.println(leaderCard.getResourceRequirements().get(Resource.SHIELD));
    }

}