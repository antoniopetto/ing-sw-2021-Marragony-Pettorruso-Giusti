package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDecks;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.FaithTrack;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.server.model.shared.MarketBoard;
import it.polimi.ingsw.server.model.singleplayer.SoloRival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Game {

    private boolean lastRound = false;
    private boolean preTurn = true;
    private boolean inserting = false;
    private boolean postTurn = false;
    private final boolean singlePlayer;
    private Player playing;

    private final VirtualView virtualView;
    private final Optional<SoloRival> soloRival;
    private final List<Player> players = new ArrayList<>();
    private final MarketBoard marketBoard = new MarketBoard();
    private DevelopmentCardDecks developmentCardDecks;
    private FaithTrack faithTrack;
    private List<Marble> marbleBuffer = new ArrayList<>();

    public static Game newSinglePlayerGame(String username, VirtualView virtualView) {
        return new Game(username, virtualView);
    }

    public static Game newRegularGame(List<String> usernames, VirtualView virtualView) {
        return new Game(usernames, virtualView);
    }

    private Game(String username, VirtualView virtualView) {
        this.virtualView = virtualView;
        singlePlayer = true;
        players.add(new Player(username));
        soloRival = Optional.of(new SoloRival());
    }

    private Game(List<String> usernames, VirtualView virtualView) {

        this.virtualView = virtualView;
        singlePlayer = false;
        if (usernames.size() > 4 || usernames.size() < 2)
            throw new IllegalArgumentException("Number of players out of bounds");

        soloRival = Optional.empty();

        for (String s : usernames) {
            players.add(new Player(s));
        }
        Collections.shuffle(players);
        playing = players.get(0);
    }

    public void singlePlayerTurn()
    {
        if(soloRival.isPresent())
            soloRival.get().soloTurn(this);
        else
            throw new IllegalStateException("Not a single player game");
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
                faithTrack.advance(playing);
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
        Resource resource;
        if(!marble.equals(Marble.WHITE))
            resource = marble.getResource();
        else
        {
            if(playing.getWhiteMarbleAliases().size()==1)
                resource=playing.getWhiteMarbleAliases().iterator().next();
            else throw new IllegalStateException("Wrong method");
        }
        return putResource(marble, depot, resource);

    }

    /**
     * This method tries to put a resource in a depot. The method is used when a player has a white marble and more
     * than one leader card with the white marble ability played, so it has to chose a resource from
     * <code>whiteMarbleAliases</code>.
     * @param marble is the marble selected from <code>marbleBuffer</code>.
     * @param depot is the depot where the player tries to put the resource.
     * @param resource is the resource to put in the depot.
     * @return false if the resource is not insertable, else it puts the resource and returns true.
     * @throws ElementNotFoundException if <code>marble</code> is not in <code>marbleBuffer</code>
     */
    public boolean putResource(Marble marble, DepotName depot, Resource resource) throws ElementNotFoundException {
        if(!marble.equals(Marble.WHITE)&&!(marble.getResource().equals(resource))) throw new IllegalArgumentException();
        if(marble.equals(Marble.WHITE)&&!playing.getWhiteMarbleAliases().contains(resource)) throw new IllegalArgumentException();
        int listId = findMarble(marble);
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
        faithTrack.advanceAllBut(playing);
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

    public void endTurn()
    {
        if (!postTurn){
            virtualView.sendError("Cannot end turn before main move");
        }
        else {
            int nextIndex = (players.indexOf(playing) + 1) % players.size();
            if (lastRound && nextIndex == 0)
                virtualView.endGame();
            else
                playing = players.get(nextIndex);
        }
    }

    public Player getPlaying() {
        return playing;
    }

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }

    public Optional<SoloRival> getSoloRival() {
        return soloRival;
    }

    public FaithTrack getTrack() {
        return faithTrack;
    }

    public MarketBoard getMarketBoard() {
        return marketBoard;
    }

    public DevelopmentCardDecks getDevelopmentCardDecks() {
        return developmentCardDecks;
    }
}
