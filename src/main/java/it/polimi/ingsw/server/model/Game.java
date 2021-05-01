package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;
import it.polimi.ingsw.server.model.exceptions.IllegalConfigXMLException;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.FaithTrack;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.server.model.shared.MarketBoard;
import it.polimi.ingsw.server.model.singleplayer.SoloRival;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.*;

public class Game {

    private enum State {
        INITIALIZING,
        PRETURN,
        INSERTING,
        POSTTURN
    }

    private static final String CONFIG_PATH = "resources/config.xml";
    private State state = State.INITIALIZING;
    private boolean lastRound = false;
    private final boolean singlePlayer;
    private Player playing;

    private final VirtualView virtualView;
    private final SoloRival soloRival;
    private final List<Player> players = new ArrayList<>();
    private final MarketBoard marketBoard = new MarketBoard();
    private final FaithTrack faithTrack;
    private final List<Marble> marbleBuffer = new ArrayList<>();
    private DevelopmentCardDecks developmentCardDecks;


    public static Game newSinglePlayerGame(String username, VirtualView virtualView) {
        return new Game(username, virtualView);
    }

    public static Game newRegularGame(List<String> usernames, VirtualView virtualView) {
        return new Game(usernames, virtualView);
    }

    private Game(String username, VirtualView virtualView) {

        state = State.INITIALIZING;
        this.virtualView = virtualView;
        singlePlayer = true;
        soloRival = new SoloRival();
        players.add(new Player(username));
        playing = players.get(0);
        faithTrack = new FaithTrack(this, virtualView, List.of(players.get(0), soloRival));
        initCards();
    }

    private Game(List<String> usernames, VirtualView virtualView) {

        state = State.INITIALIZING;
        this.virtualView = virtualView;
        singlePlayer = false;
        soloRival = null;

        if (usernames.size() > 4 || usernames.size() < 2)
            throw new IllegalArgumentException("Number of players out of bounds");

        for (String s : usernames)
            players.add(new Player(s));

        Collections.shuffle(players);
        playing = players.get(0);
        faithTrack = new FaithTrack(this, virtualView, players);
        initCards();
    }

    private void initCards(){
        try {
            CardParser cardParser = new CardParser(CONFIG_PATH);

            List<DevelopmentCard> developmentCards = cardParser.parseDevelopmentCards();

            Collections.shuffle(developmentCards);
            developmentCardDecks = new DevelopmentCardDecks(cardParser.parseDevelopmentCards());

            List<LeaderCard> leaderCards = cardParser.parseLeaderCards();
            Collections.shuffle(leaderCards);
            for(Player p : players){
                List<LeaderCard> firstFour = leaderCards.subList(0, 3);
                p.setLeaderCards(firstFour);
                //note: firstFour is backed by the original list
                firstFour.clear();
            }

            ProductionPower basePower = cardParser.parseBaseProductionPower();
            for(Player p : players)
                p.getPlayerBoard().addExtraProductionPower(basePower);
        }
        catch (ParserConfigurationException | IOException | SAXException | IllegalConfigXMLException e){
            e.printStackTrace();
            System.out.println("Parsing error");
            virtualView.exitGame();
        }
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
     */
    public boolean putResource(Marble marble, DepotName depot){
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
     */
    public boolean putResource(Marble marble, DepotName depot, Resource resource) {
        int listId;
        try{
            if(!marble.equals(Marble.WHITE)&&!(marble.getResource().equals(resource))) throw new IllegalArgumentException("Wrong request");
            if(marble.equals(Marble.WHITE)&&!playing.getWhiteMarbleAliases().contains(resource)) throw new IllegalArgumentException("White marble not associated to that resource");
            listId = findMarble(marble);
        }catch (Exception e)
        {
            virtualView.sendError(e.getMessage());
            return false;
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
     */
    public void discard(Marble marble)  {
        try{
            int listId = findMarble(marble);
            marbleBuffer.remove(listId);
            faithTrack.advanceAllBut(playing);
        }catch (Exception e)
        {
            virtualView.sendError(e.getMessage());
        }

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
        if(!isPresent) throw new ElementNotFoundException("Marble not playable");
        return listId;
    }

    public void switchDepots(DepotName depot1, DepotName depot2)
    {
        if(!state.equals(State.INSERTING)) virtualView.sendError("Wrong state of the application");
        else {
            try {
                playing.getPlayerBoard().getWareHouse().switchDepots(depot1, depot2);
            } catch (Exception e) {
                virtualView.sendError(e.getMessage());
            }
        }
    }

    public void moveDepots( DepotName depotFrom, DepotName depotTo){
        if(!state.equals(State.INSERTING)) virtualView.sendError("Cannot move resources between depots now");
            else{
                try{
                    playing.getPlayerBoard().getWareHouse().moveDepots(depotFrom, depotTo);
                } catch (IllegalStateException | IllegalArgumentException e){
                    virtualView.sendError(e.getMessage());
                }
        }
    }

    public void endTurn()
    {
        if (!state.equals(State.POSTTURN)){
            virtualView.sendError("Cannot end turn before main move");
        }
        else {
            int nextIndex = (players.indexOf(playing) + 1) % players.size();
            if (lastRound && nextIndex == 0)
                virtualView.endGame();
            else
                playing = players.get(nextIndex);
                state = State.PRETURN;
                if (singlePlayer)
                    soloRival.soloTurn(this);
        }
    }

    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        if (!state.equals(State.PRETURN))
            virtualView.sendError("Cannot activate production now");
        else if (!playing.getPlayerBoard().canActivateProduction(selectedCardIds, selectedExtraPowers))
            virtualView.sendError("Cannot afford this production");
        else playing.getPlayerBoard().activateProduction(selectedCardIds, selectedExtraPowers);
        state = State.POSTTURN;
    }

    public void playLeaderCard(int cardId){
        if(state.equals(State.INSERTING) || state == State.INITIALIZING)
            virtualView.sendError("Cannot play LeaderCard now");
        else{
            try {
                if(!getPlaying().playLeaderCard(cardId))
                    virtualView.sendError("The player does not meet the requirements");
            }catch (IllegalStateException | IllegalArgumentException | ElementNotFoundException e){
                virtualView.sendError(e.getMessage());
            }

        }
    }

    public void buyandAddCardInSlot(CardColor cardColor, int level, int slotId){

        if(state != State.INSERTING) virtualView.sendError("Cannot Insert DevCard now");
        else {
            DevelopmentCard developmentCard = developmentCardDecks.readTop(cardColor, level);
            try{
                getPlaying().addCard(developmentCard,slotId);
            }catch (IllegalArgumentException e){
                virtualView.sendError(e.getMessage());
            }
            developmentCardDecks.drawCard(cardColor, level);

        }
    }

    public void setLastRound(boolean lastRound){
        this.lastRound = lastRound;
    }

    public Player getPlaying() {
        return playing;
    }

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }

    public Optional<SoloRival> getSoloRival() { return Optional.of(soloRival); }

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
