package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardColor;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.DevelopmentCardDecks;
import it.polimi.ingsw.model.cards.ResourceRequirement;
import it.polimi.ingsw.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.model.playerboard.DepotName;
import it.polimi.ingsw.model.playerboard.Resource;
import it.polimi.ingsw.model.shared.FaithTrack;
import it.polimi.ingsw.model.shared.Marble;
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
    private final MarketBoard marketBoard = new MarketBoard();
    private DevelopmentCardDecks developmentCardDecks;
    private FaithTrack track;
    private Player playing;
    private List<Marble> marbleBuffer = new ArrayList<>();

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

    /**
     * This method is called when <code>playing</code> buys resources from <code>MarketBoard</code>.
     * It takes all the marbles from a row/column of the market board and checks the color of the marble. If
     * it's red the position of <code>playing</code> advances, if it white it checks if there are
     * <code>whiteMarbleAliases</code> in <code>playing</code> and in that case it's added in <code>marbleBuffer</code>.
     * Marbles of all the other colors are added in <code>marbleBuffer</code>.
     * @param idLine is the id of the line to buy in the <code>MarketBoard</code>
     * @param isRow is a boolean which tells whether the line is a row or a column.
     */
    public void buyResources(int idLine, boolean isRow){
        List<Marble> marbles = playing.buyResources(this, idLine, isRow);
        for (Marble marble:marbles) {
            if(marble.equals(Marble.RED))
            {
                track.advance(playing);
            }
            else if(marble.equals(Marble.WHITE))
            {
                if(!playing.getWhiteMarbleAliases().isEmpty())
                    marbleBuffer.add(marble);
            }
            else
                marbleBuffer.add(marble);
        }
    }

    /**
     * This method try to put the equivalent resource of a marble in a specific depot in the warehouse.
     * @param marble is the marble selected by the player
     * @param depot is the depot where the player tries to add the resource
     * @return false if it isn't possible to add the resource in <code>depot</code>, else it add the resource and return true
     * @throws ElementNotFoundException if <code>marble</code> is not in <code>marbleBuffer</code>
     */
    public boolean putResource(Marble marble, DepotName depot) throws ElementNotFoundException {
        int listId = findMarble(marble);
        Resource resource;
        if(!marble.equals(Marble.WHITE))
            resource = marble.getResource();
        else
        {
            if(playing.getWhiteMarbleAliases().size()==1)
                resource=playing.getWhiteMarbleAliases().iterator().next();
            else
            {
                List<Resource> resources = new ArrayList<>();
                while(playing.getWhiteMarbleAliases().iterator().hasNext())
                    resources.add(playing.getWhiteMarbleAliases().iterator().next());
                resource = choseBetweenResources(resources);
            }

        }
        if(!playing.getPlayerBoard().getWareHouse().isInsertable(depot, resource)) return false;
        playing.getPlayerBoard().getWareHouse().insert(depot, resource);
        marbleBuffer.remove(listId);
        return true;
    }

    /**
     * This method is called when it is not possible to add the equivalent resource of a marble in the warehouse
     * so the marble is discarded.
     * @param marble is the marble to discard
     * @throws ElementNotFoundException if <code>marble</code> is not in <code>marbleBuffer</code>
     */
    public void discard(Marble marble) throws ElementNotFoundException {
        int listId = findMarble(marble);
        marbleBuffer.remove(listId);
        track.advanceAllBut(playing);
    }

    /**
     * This method checks if <code>marble</code> is present in <code>marbleBuffer</code> and returns its position in it.
     * @param marble is the marble to check.
     * @return the position of <code>marble</code> in <code>marbleBuffer</code>
     * @throws ElementNotFoundException if <code>marble</code> is not i <code>marbleBuffer</code>
     */
    private int findMarble(Marble marble) throws ElementNotFoundException {
        if(marbleBuffer.isEmpty()) throw new IllegalStateException("No more marbles to handle");
        boolean isPresent = false;
        int listId=0;
        while(listId<marbleBuffer.size()&&!isPresent)
        {
            if(marble.equals(marbleBuffer.get(listId)))
                isPresent=true;
            else listId++;
        }
        if(!isPresent) throw new ElementNotFoundException();
        return listId;
    }

    public Resource choseBetweenResources(List<Resource> resouces)
    {
        Resource resourceChosen;
        //messaggio all'utente
        resourceChosen=resouces.get(0); //temporaneo
        return resourceChosen;
    }





    /*
    //DA SPOSTARE NEL CONTROLLER E DA CAMBIARE
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

    public void setDevelopmentCardDecks(List<DevelopmentCard> list) {
        this.developmentCardDecks = new DevelopmentCardDecks(list);
    }

    public void setTrack() {
        List<AbstractPlayer> playerList = new ArrayList<>(players);
        soloRival.ifPresent(playerList::add);
        //this.track = new FaithTrack(playerList, null, null);
        //TODO complete the creation of the track with non null parameters
    }
}
