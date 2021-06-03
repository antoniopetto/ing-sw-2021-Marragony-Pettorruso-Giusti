package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderCardTest {
    private LeaderCard card;
    private final SpecialAbility ability = new CardDiscountAbility(Resource.COIN);
    private final Player player = new Player("AAA", Mockito.mock(VirtualView.class));

    @Before
    public void setUp() {

        List<ResourceRequirement> requirements = new ArrayList<>();
        requirements.add(new ResourceRequirement(Resource.SHIELD, 1));
        card = new LeaderCard(3,1, requirements, ability);

        ProductionPower power = new ProductionPower(1, 1);

        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(1,1, 1, CardColor.BLUE, requirements, power), 0);
        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(2,1, 2, CardColor.BLUE, requirements, power), 0);
        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(3,1, 3, CardColor.GREEN, requirements, power), 0);

        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(4,1, 1, CardColor.GREEN, requirements, power), 1);
        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(5,1, 2, CardColor.PURPLE, requirements, power), 1);
        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(6,1, 3, CardColor.YELLOW, requirements, power), 1);

        player.getPlayerBoard().addCardInSlot(new DevelopmentCard(7,1, 1, CardColor.BLUE, requirements, power), 2);

        player.getPlayerBoard().getStrongBox().addResource(Resource.COIN, 2);
        player.getPlayerBoard().getStrongBox().addResource(Resource.SHIELD, 3);
        player.getPlayerBoard().getStrongBox().addResource(Resource.STONE, 1);
    }

    @Test
    public void constructorTest() {
        //checks that it is impossible to create a leader card with a negative number of victory points
        try {
            card = new LeaderCard(1, -1, null,null);
            fail();
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        //checks that it is impossible to create a leader card without requirement or power
        try{
            card = new LeaderCard(1, 2, null, null);
            fail();
        }catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        List<Requirement> requirements = new ArrayList<>();
        try {
            card = new LeaderCard(2, 1,requirements,ability);
        }catch (Exception e) {
            fail();
        }
    }

    @Test
    public void playTest() {
        try{
            card.play(player);
        }catch (NullPointerException e) {
            assertTrue(true);
        }catch (Exception e1) {
            fail();
        }
        assertTrue(card.isPlayed());
        assertTrue(player.getActiveDiscount().contains(Resource.COIN));
    }

    @Test
    public void isPlayableTest() {
        assertTrue(card.isPlayable(player));
        List<CardRequirement> requirements = new ArrayList<>();
        requirements.add(new CardRequirement(CardColor.BLUE, 2));
        requirements.add((new CardRequirement(CardColor.GREEN,3,1)));
        LeaderCard card1 = new LeaderCard(1,1,requirements, ability);
        assertTrue(card1.isPlayable(player));

        List<ResourceRequirement> resRequirements = new ArrayList<>();
        resRequirements.add(new ResourceRequirement(Resource.SERVANT,1));
        resRequirements.add(new ResourceRequirement(Resource.SHIELD, 2));
        LeaderCard card2 = new LeaderCard(1,1, resRequirements, ability);
        assertFalse(card2.isPlayable(player));
    }
}