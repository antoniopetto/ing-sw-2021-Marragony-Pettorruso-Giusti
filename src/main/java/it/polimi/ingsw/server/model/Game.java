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

        this.virtualView = virtualView;
        singlePlayer = true;
        soloRival = new SoloRival();
        Player player = new Player(username);
        player.setObserver(virtualView);
        players.add(player);
        playing = players.get(0);
        faithTrack = new FaithTrack(this, virtualView, List.of(players.get(0), soloRival));
        initCards();

    }

    private Game(List<String> usernames, VirtualView virtualView) {

        this.virtualView = virtualView;
        singlePlayer = false;
        soloRival = null;

        if (usernames.size() > 4 || usernames.size() < 2)
            throw new IllegalArgumentException("Number of players out of bounds");

        for (String s : usernames){
            Player player = new Player(s);
            player.setObserver(virtualView);
            players.add(new Player(s));
        }

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
                List<LeaderCard> firstFour = leaderCards.subList(0, 4);
                p.setLeaderCards(firstFour);
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

        if (state != State.PRETURN){
            virtualView.sendError("Illegal state command");
            return;
        }

        List<Marble> marbles = playing.buyResources(this, idLine, isRow);
        for (Marble marble : marbles) {
            if(marble.equals(Marble.RED)) {
                faithTrack.advance(playing);
            }
            else if(marble.equals(Marble.WHITE)) {
                if(!playing.getWhiteMarbleAliases().isEmpty())
                    marbleBuffer.add(marble);
            }
            else
                marbleBuffer.add(marble);
        }

        if(marbleBuffer.size() > 0){
            virtualView.createBuffer(marbleBuffer);
            state = State.INSERTING;
        }
        else
            state = State.POSTTURN;
    }

    /**
     * This method tries to put the equivalent resource of a marble in a specific depot in the warehouse.
     * @param marble is the marble selected by the player
     * @param depot is the depot where the player tries to add the resource
     */
    public void putResource(Marble marble, DepotName depot){

        if (marble == Marble.WHITE)
            virtualView.sendError("Can't add a white marble without chosen resource");

        putResource(marble, depot, marble.getResource());
    }

    /**
     * This method tries to put a resource in a depot. The method is used when a player has a white marble and more
     * than one leader card with the white marble ability played, so it has to chose a resource from
     * <code>whiteMarbleAliases</code>.
     * @param marble is the marble selected from <code>marbleBuffer</code>.
     * @param depot is the depot where the player tries to put the resource.
     * @param resource is the resource to put in the depot.
     */
    public void putResource(Marble marble, DepotName depot, Resource resource){

        if (state != State.INSERTING) {
            virtualView.sendError("Illegal state command");
            return;
        }

        int listId = marbleBuffer.indexOf(marble);
        if (listId == -1 ||
            marble != Marble.WHITE && marble.getResource() != resource ||
            marble == Marble.WHITE && !playing.getWhiteMarbleAliases().contains(resource) ||
            !playing.getPlayerBoard().getWareHouse().isInsertable(depot, resource)) {

            virtualView.sendError("Illegal putResource request");
            return;
        }

        playing.getPlayerBoard().getWareHouse().insert(depot, resource);
        marbleBuffer.remove(listId);
        virtualView.bufferUpdate(marble);

        if (marbleBuffer.size() == 0)
            state = State.POSTTURN;
    }

    /**
     * This method is called when it is not possible to add the equivalent resource of a marble in the warehouse
     * so the marble is discarded.
     * @param marble is the marble to discard
     */
    public void discardMarble(Marble marble)  {

        if (state != State.INSERTING){
            virtualView.sendError("Illegal state command");
            return;
        }

        int listId = marbleBuffer.indexOf(marble);
        if (listId == -1) {
            virtualView.sendError("Illegal discardMarble request");
            return;
        }

        marbleBuffer.remove(listId);
        faithTrack.advanceAllBut(playing);
        virtualView.bufferUpdate(marble);

        if (marbleBuffer.size() == 0){
            state = State.POSTTURN;
        }
    }

    public void switchDepots(DepotName depot1, DepotName depot2) {

        if(state != State.INSERTING){
            virtualView.sendError("Illegal state command");
            return;
        }
        try {
            playing.getPlayerBoard().getWareHouse().switchDepots(depot1, depot2);
        } catch (Exception e) {
            virtualView.sendError(e.getMessage());
        }
    }

    public void moveDepots(DepotName depotFrom, DepotName depotTo) {

        if (!state.equals(State.INSERTING)){
            virtualView.sendError("Illegal state command");
            return;
        }
        try{
            playing.getPlayerBoard().getWareHouse().moveDepots(depotFrom, depotTo);
        } catch (IllegalStateException | IllegalArgumentException e) {
            virtualView.sendError(e.getMessage());
        }
    }

    public void endTurn(){
        if (state != State.POSTTURN && state != State.INITIALIZING){
            virtualView.sendError("Illegal state command");
            return;
        }
        int nextIndex = (players.indexOf(playing) + 1) % players.size();
        if (nextIndex == 0 && lastRound)
            virtualView.endGame();
        else{
            playing = players.get(nextIndex);
            if (state == State.POSTTURN || nextIndex == 0)
                state = State.PRETURN;
            if (singlePlayer)
                soloRival.soloTurn(this);
            virtualView.startPlay();
        }
    }

    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){

        if (!state.equals(State.PRETURN)) {
            virtualView.sendError("Illegal state command");
            return;
        }

        if (!playing.getPlayerBoard().canActivateProduction(selectedCardIds, selectedExtraPowers)) {
            virtualView.sendError("Cannot afford this production");
            return;
        }

        playing.getPlayerBoard().activateProduction(selectedCardIds, selectedExtraPowers);
        state = State.POSTTURN;
    }

    public void playLeaderCard(int cardId){

        if(state != State.PRETURN && state != State.POSTTURN){
            virtualView.sendError("Illegal state command");
            return;
        }

        try {
            if(!playing.playLeaderCard(cardId))
                virtualView.sendError("The player does not meet the requirements");
        } catch (IllegalStateException | IllegalArgumentException e){
            virtualView.sendError(e.getMessage());
        }
    }

    public void discardLeaderCard(int cardId) {

        if (state != State.PRETURN && state != State.POSTTURN){
            virtualView.sendError("Illegal state command");
            return;
        }
        try {
            playing.removeLeaderCard(cardId);
            if(state == State.INITIALIZING)
                if (playing.getLeaderCardList().size() == 2)
                    endTurn();
            else
                faithTrack.advance(playing);
        } catch (ElementNotFoundException e){
            virtualView.sendError("Leader card not found");
        }
    }

    public void setLastRound() {
        this.lastRound = true;
    }

    public void buyAndAddCardInSlot(CardColor cardColor, int level, int slotId){

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

    public Player getPlaying() {
        return playing;
    }

    public List<Marble> getMarbleBuffer() {
        return marbleBuffer;
    }

    public Optional<SoloRival> getSoloRival() {
        return Optional.of(soloRival);
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