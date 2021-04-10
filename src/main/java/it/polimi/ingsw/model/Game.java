package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.DevelopmentCardDecks;
import it.polimi.ingsw.model.cards.ResourceRequirement;
import it.polimi.ingsw.model.playerboard.Resource;
import it.polimi.ingsw.model.shared.FaithTrack;
import it.polimi.ingsw.model.shared.MarketBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Game {
    private boolean endgame;
    private final Optional<SoloRival> soloRival;
    private final boolean singlePlayer;
    private final List<Player> players = new ArrayList<>();
    private MarketBoard marketBoard = new MarketBoard();
    private DevelopmentCardDecks developmentCardDecks;
    private FaithTrack track;

    public Optional<SoloRival> getSoloRival() {
        return soloRival;
    }

    public FaithTrack getTrack() {
        return track;
    }

    public static Game newSinglePlayerGame(String username) {
        return new Game(username);
    }

    public static Game newRegularGame(List<String> usernames) {
        return new Game(usernames);
    }

    private Game(String username) {
        singlePlayer = true;
        players.add(new Player(username));
        soloRival = Optional.of(new SoloRival());
    }


    private Game(List<String> usernames) {
        if (usernames.size() > 4 || usernames.size() < 2)
            throw new IllegalArgumentException("Number of players out of bounds");

        singlePlayer = false;
        soloRival = Optional.empty();

        for (String s : usernames) {
            players.add(new Player(s));
        }
        Collections.shuffle(players);
    }

    private Player findPlayer(String username) {
        return players.stream()
                .filter(player -> username.equals(player.getUsername()))
                .findFirst().orElse(null);
    }

    public void singlePlayerTurn()
    {
        if(soloRival.isPresent())
            soloRival.get().soloTurn(this);
        else
            throw new IllegalStateException("Not a single player game");
    }
    public MarketBoard getMarketBoard() {
        return marketBoard;
    }

    public DevelopmentCardDecks getDevelopmentCardDecks() {
        return developmentCardDecks;
    }

    public boolean tryBuyCard(Player player, CardColor cardColor, int level) {

        boolean isPossible = false;
        /*
        DevelopmentCard developmentCard = developmentCardDecks.readTop(cardColor, level);
        ResourceRequirement resourceRequirement = developmentCard.getRequirement();
        ResourceRequirement resourceRequirementDiscount;
        int n = resourceRequirement.getQuantity();
        for (Resource resource : player.getActiveDiscount())
            if (resourceRequirement.getResource().equals(resource)) n--;

            resourceRequirementDiscount = new ResourceRequirement(resourceRequirement.getResource(), n);




        isPossible = resourceRequirementDiscount.isSatisfied(player);

        if(isPossible){

        }*/
        return isPossible;
    }



    /*
    private int idSlot = 0; // da passare nel costruttore

    public void insertCardInSlot(DevelopmentCard dCard, String user){

        Player player = findPlayer(user);
        if( player.tryBuyDevelopmentCard(dCard) ) {
            boolean allSlots = false;

            for(int i = 0; i < 3; i++){
                player.selectIdSlot(idSlot);
                if(player.tryToAddDevelopmentCard(dCard)){
                    player.getPlayerBoard().pay(dCard.getRequirement());
                    allSlots = true;
                    break;
                }
            }

            if(!allSlots) System.out.println("The DevelopmentCard cannot be inserted in any slot");

        }
        else System.out.println("The" + user + "Player hasn't enough resources to pay the DevelopmentCard dCard");

      }
    */

    public boolean isSinglePlayer() { return singlePlayer; }

}
