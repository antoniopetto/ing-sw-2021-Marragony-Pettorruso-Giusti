package it.polimi.ingsw.server;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Player;
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
import java.util.stream.Collectors;

public class GameController {

    private enum State {
        INITIALIZING,
        PRETURN,
        INSERTING,
        POSTTURN
    }

    private State state = State.INITIALIZING;
    private boolean lastRound = false;
    private final boolean singlePlayer;
    private Player playing;

    private final VirtualView virtualView;
    private final SoloRival soloRival;
    private final List<Player> players = new ArrayList<>();
    private final MarketBoard marketBoard;
    private final FaithTrack faithTrack;
    private final DevelopmentCardDecks developmentCardDecks;
    private final List<Marble> marbleBuffer = new ArrayList<>();

    public GameController(Set<String> usernames, VirtualView virtualView) {

        if (usernames.size() == 0 || usernames.size() > 4 || virtualView == null)
            throw new IllegalArgumentException();

        this.virtualView = virtualView;

        marketBoard = new MarketBoard(virtualView);
        developmentCardDecks = new DevelopmentCardDecks(parseDevCards(), virtualView);
        faithTrack = new FaithTrack(virtualView);

        for (String username : usernames){
            Player player = new Player(username, virtualView);
            players.add(player);
        }

        Collections.shuffle(players);
        playing = players.get(0);
        initLeaderCards();

        if (usernames.size() == 1) {
            singlePlayer = true;
            soloRival = new SoloRival();
            faithTrack.addPlayers(List.of(soloRival, players.get(0)));
        }
        else {
            singlePlayer = false;
            soloRival = null;
            faithTrack.addPlayers(players);
        }
    }

    private List<DevelopmentCard> parseDevCards(){
        try {
            return (new CardParser()).parseDevelopmentCards();
        }
        catch (ParserConfigurationException | IOException | SAXException | IllegalConfigXMLException e){
            e.printStackTrace();
            virtualView.exitGame();
            return null;
        }
    }

    private void initLeaderCards(){
        try {
            CardParser parser = new CardParser();
            List<LeaderCard> leaderCards = parser.parseLeaderCards();
            Collections.shuffle(leaderCards);
            for(Player p : players){
                List<LeaderCard> firstFour = leaderCards.subList(0, 4);
                p.setLeaderCards(firstFour);
                firstFour.clear();
            }
        }
        catch (ParserConfigurationException | IOException | SAXException | IllegalConfigXMLException e){
            e.printStackTrace();
            virtualView.exitGame();
        }
    }

    /**
     * Command that discards a <code>LeaderCard</code>, hence making all the other player advance.
     *
     * @param cardId        The id of the card to discard
     */
    public void discardLeaderCard(int cardId) {

        boolean isPostTurn = state.equals(State.POSTTURN);

        if (state == State.INSERTING){
            virtualView.sendError("Illegal state command");
            virtualView.nextAction(isPostTurn);
            return;
        }

        try {
            playing.removeLeaderCard(cardId);

            if (state != State.INITIALIZING) {
                advance(playing);
                virtualView.nextAction(isPostTurn);
            }
            else if (playing.getLeaderCardList().size() > 2) {
                virtualView.requestDiscardLeaderCard();
            }
            else {
                int position = turnPosition(playing.getUsername());
                if (position == 0)
                    endTurn();
                else {
                    playing.addAllWhiteMarbleAlias();
                    marbleBuffer.add(Marble.WHITE);
                    if (position == 3)
                        marbleBuffer.add(Marble.WHITE);
                    virtualView.createBuffer(marbleBuffer);
                    virtualView.requestPutResource();
                }
            }
        } catch (ElementNotFoundException e){
            virtualView.sendError("Leader card not found");
            virtualView.nextAction(isPostTurn);
        } catch (IllegalArgumentException e){
            virtualView.sendError(e.getMessage());
            virtualView.nextAction(isPostTurn);
        }
    }

    /**
     * This method is called when <code>playing</code> buys resources from <code>MarketBoard</code>.
     * It takes all the marbles from a row/column of the market board and checks the color of the marble. If
     * it's red the position of <code>playing</code> advances, if it white it checks if there are
     * <code>whiteMarbleAliases</code> in <code>playing</code> and in that case it's added in <code>marbleBuffer</code>.
     * Marbles of all the other colors are added in <code>marbleBuffer</code>.
     *
     * @param idLine is the id of the line to buy in the <code>MarketBoard</code>
     * @param isRow is a boolean which tells whether the line is a row or a column.
     */
    public void buyResources(int idLine, boolean isRow){

        if (state != State.PRETURN){
            virtualView.sendError("Illegal state command");
            return;
        }

        List<Marble> marbles = playing.buyResources(marketBoard, idLine, isRow);
        for (Marble marble : marbles) {
            if(marble.equals(Marble.RED)) {
                advance(playing);
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
            virtualView.manageResource();
        }
        else{
            state = State.POSTTURN;
            virtualView.nextAction(true);
        }

    }

    /**
     * This method tries to put the equivalent resource of a marble in a specific depot in the warehouse.
     *
     * @param marble    is the marble selected by the player
     * @param depot     is the depot where the player tries to add the resource
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
     *
     * @param marble is the marble selected from <code>marbleBuffer</code>.
     * @param depot is the depot where the player tries to put the resource.
     * @param resource is the resource to put in the depot.
     */
    public void putResource(Marble marble, DepotName depot, Resource resource){

        if (state != State.INSERTING && state != State.INITIALIZING) {
            virtualView.sendError("Illegal state command");
            return;
        }

        int listId = marbleBuffer.indexOf(marble);

        if (listId == -1 ||
            marble != Marble.WHITE && marble.getResource() != resource ||
            marble == Marble.WHITE && !playing.getWhiteMarbleAliases().contains(resource) ||
            !playing.getPlayerBoard().getWareHouse().isInsertable(depot, resource)) {

            virtualView.sendError("Illegal putResource request");
            if (state == State.INITIALIZING)
                virtualView.requestPutResource();
            else
                virtualView.manageResource();
            return;
        }

        playing.getPlayerBoard().getWareHouse().insert(depot, resource);
        marbleBuffer.remove(listId);
        virtualView.bufferUpdate(marble);

        if (marbleBuffer.size() > 0 && state == State.INSERTING)
            virtualView.manageResource();
        else if (marbleBuffer.size() > 0 && state == State.INITIALIZING)
            virtualView.requestPutResource();
        else if (state == State.INSERTING) {
            state = State.POSTTURN;
            virtualView.nextAction(true);
        }
        else if (state == State.INITIALIZING){
            playing.clearWhiteMarbleAlias();
            endTurn();
        }
    }
    public void goBack() {
        if (state == State.PRETURN)
            virtualView.nextAction(false);
        else if (state == State.POSTTURN)
            virtualView.nextAction(true);
        else if (state == State.INSERTING)
            virtualView.manageResource();
    }

    /**
     * Command to discard marble.
     * This method is called when the playing user chooses not to store the equivalent resource of a marble
     * in the warehouse, so the marble is discarded, and all the other players advance.
     *
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

        if (marbleBuffer.size() > 0)
            virtualView.manageResource();
        else {
            state = State.POSTTURN;
            virtualView.nextAction(true);
        }
    }

    /**
     * Command that makes the current player buy a card and place it in the selected slot in the <code>PlayerBoard</code>
     *
     * @param cardColor     The color of the selected <code>CardDeck</code>
     * @param level         The level of the selected <code>CardDeck</code>
     * @param slotId        The id of the destination slot
     */
    public void buyAndAddCardInSlot(CardColor cardColor, int level, int slotId){

        if(state != State.PRETURN) {
            virtualView.sendError("Cannot Insert DevCard now");
            virtualView.nextAction(false);
            return;
        }

        DevelopmentCard developmentCard;
        try {
            developmentCard = developmentCardDecks.readTop(cardColor, level);
        }catch (EmptyStackException e) {
            virtualView.sendError("There are no more DevelopmentCard with that color and level");
            virtualView.nextAction(false);
            return;
        }

        try{
            getPlaying().addCard(developmentCard,slotId);
        } catch (IllegalArgumentException e) {
            virtualView.sendError(e.getMessage());
            virtualView.nextAction(false);
            return;
        }

        if (getPlaying().countDevCards() == 7)
            lastRound = true;
        developmentCardDecks.drawCard(cardColor, level);
        state = State.POSTTURN;

        virtualView.nextAction(true);
    }

    /**
     * Activates the production power of a set of cards/extra production powers in the active player playerboard.
     * The player can call this command only if he is in PreTurn, and the command does something only if the entire
     * set of selected production powers is affordable by the player.
     *
     * @param selectedCardIds       Set of ids of the development cards the player wants to activate
     * @param selectedExtraPowers   Map from index of <code>extraProductionPower</code> to desired concrete i/o resources
     */
    public void activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){

        if (!state.equals(State.PRETURN)) {
            virtualView.sendError("Illegal state command");
            return;
        }
        try {
            int steps = playing.activateProduction(selectedCardIds, selectedExtraPowers);
            for (int i = 0; i < steps; i++)
                faithTrack.advance(playing);
            state = State.POSTTURN;
            virtualView.nextAction(true);
        } catch (IllegalArgumentException e){
            virtualView.sendError(e.getMessage());
            virtualView.nextAction(false);
        }
    }

    /**
     * Switches the content of two depots of the playing user.
     *
     * @param depot1    The first depot to switch
     * @param depot2    The second depot to switch
     */
    public void switchDepots(DepotName depot1, DepotName depot2) {

        if(state != State.INSERTING){
            virtualView.sendError("Illegal state command");
            virtualView.nextAction(false);
            return;
        }
        try {
            playing.getPlayerBoard().getWareHouse().switchDepots(depot1, depot2);
        } catch (Exception e) {
            virtualView.sendError(e.getMessage());
        }
        virtualView.manageResource();
    }

    /**
     * Command to move the content of a depot in another one.
     * In the warehouse of the playing user, moves the resources of <code>depotFrom</code> into <code>depotTo</code>.
     * Stops if the destination reaches full capacity, and requires the depots to have matching resources.
     *
     * @param depotFrom     The source depot
     * @param depotTo       The destination depot
     */
    public void moveDepots(DepotName depotFrom, DepotName depotTo) {

        if (!state.equals(State.INSERTING)){
            virtualView.sendError("Illegal state command");
            virtualView.nextAction(false);
            return;
        }
        try{
            playing.getPlayerBoard().getWareHouse().moveDepots(depotFrom, depotTo);
        } catch (IllegalStateException | IllegalArgumentException e) {
            virtualView.sendError(e.getMessage());
        }

        virtualView.manageResource();
    }



    /**
     * Command that activates a <code>LeaderCard</code> of the playing user.
     * It only works if the card requirements are satisfied and if the game is in the correct state.
     *
     * @param cardId        The id of the card to activate
     */
    public void playLeaderCard(int cardId){

        if(state != State.PRETURN && state != State.POSTTURN){
            virtualView.sendError("Illegal state command");
            virtualView.nextAction(false);
            return;
        }

        try {
            if(!playing.playLeaderCard(cardId)) {
                virtualView.sendError("The player does not meet the requirements");
            }

        } catch (IllegalStateException | IllegalArgumentException  | ElementNotFoundException e){
            virtualView.sendError(e.getMessage());
        }
        virtualView.nextAction(state.equals(State.POSTTURN));
    }

    /**
     * Command called by a player when, during PostTurn, decides to end his turn.
     * It can also be called during initialization, only if the active player has the right number of leader cards (2).
     * The method also ends the game if we are in the last round and the last player has called the method.
     * In singleplayer mode, when the player ends his turn this method triggers the SoloRival turn.
     */
    public void endTurn(){
        if (state != State.POSTTURN && state != State.INITIALIZING ||
                (state == State.INITIALIZING && playing.getLeaderCardList().size() > 2)){
            virtualView.sendError("Illegal state command");
            return;
        }
        int nextIndex = (players.indexOf(playing) + 1) % players.size();

        if (nextIndex == 0 && lastRound) {
            if (!singlePlayer)
                virtualView.endGame();
            else
                virtualView.endSinglePlayerGame();
        }
        else{
            playing = players.get(nextIndex);
            if (nextIndex == 0)
                state = State.PRETURN;
            if(state == State.INITIALIZING) {
                virtualView.messageFilter(null, playing.getUsername()+" is choosing the leader cards...");
                virtualView.requestDiscardLeaderCard();
            }
            else {
                if (!singlePlayer) {
                    virtualView.startPlay();
                } else {
                    soloRival.soloTurn(this);
                    if (lastRound)
                        virtualView.endSinglePlayerGame();
                    else
                        virtualView.startPlay();
                }
            }
        }
    }

    /** Auxiliary methods */

    public Player getPlaying() {
        return playing;
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    public Optional<SoloRival> getSoloRival() {
        return Optional.of(soloRival);
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public MarketBoard getMarketBoard() {
        return marketBoard;
    }

    public DevelopmentCardDecks getDevelopmentCardDecks() {
        return developmentCardDecks;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int turnPosition(String playerUsername){
        for(int i = 0; i < players.size(); i++){
           if( players.get(i).getUsername().equals(playerUsername) ) return i;
        }
        return -1;
    }

    public Map<String, Integer> getLeaderboard(){
        List<Map.Entry<String, Integer>> pointsList = new ArrayList<>();
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        for (Player player : players)
            pointsList.add(new AbstractMap.SimpleEntry<>(player.getUsername(), player.countPoints()));
        Collections.reverse(pointsList);
        pointsList.sort(Map.Entry.comparingByValue());
        for (Map.Entry<String, Integer> entry : pointsList)
            leaderboard.put(entry.getKey(), entry.getValue());
        return leaderboard;
    }

    public boolean isSinglePlayer(){
        return singlePlayer;
    }

    public void advance(AbstractPlayer player){
        faithTrack.advance(player);
        if (faithTrack.someoneFinished())
            lastRound = true;
    }

    public SimpleModel getSimple(String requirerName){

        SimpleModel game = new SimpleModel();
        game.setPlayers(players.stream().map(Player::getSimple).collect(Collectors.toList()), requirerName);

        for (SimplePlayer player : game.getPlayers())
            if (!player.getUsername().equals(requirerName))
                player.clearLeaderCards();

        game.setMarbleBuffer(marbleBuffer);
        game.setMarketBoard(marketBoard.getMarbleGrid());
        game.setSpareMarble(marketBoard.getSpareMarble());
        game.setDevCardDecks(getDevelopmentCardDecks().getDevCardIds());

        return game;
    }
}