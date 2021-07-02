package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.messages.Msg;
import it.polimi.ingsw.shared.messages.toview.*;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.shared.CardColor;
import it.polimi.ingsw.shared.ProductionPower;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.server.model.shared.FaithTrack;
import it.polimi.ingsw.shared.Marble;
import it.polimi.ingsw.shared.messages.update.*;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class VirtualView implements Runnable{

    private File saveDir;
    private File saveFile;
    private boolean exiting = false;
    private GameController gameController;
    private final Map<String, ClientHandler> players = new HashMap<>();
    private Boolean turnJustFinished = null;

    /**
     * Creates a VirtualView and the underlying single/multi player game.
     * It looks for a save file and if there's one it restores the previous game, otherwise creates a new one.
     * @param players   The map of players with their connection parameters
     */
    public VirtualView(Map<String, ClientHandler> players){

        this.players.putAll(players);
        saveDir = new File(System.getProperty("mor.base") + "/data/saves");
        saveFile = new File(saveDir.getPath() + "/" + Server.formatGameName(players.keySet()) + ".ser");
        gameController = restoreGame()
                .orElse(new GameController(this.players.keySet()));
        gameController.setVirtualView(this);

        for (String playerName : players.keySet())
            sendPlayer(playerName, new InitModelMsg(gameController.getSimple(playerName)));

        sendAll(new TitleMsg());
        gameController.resumeGame();
    }

    /**
     * Tries to restore a save file looking for the game's unique identifier in the filesystem
     * @return  The Optional restored GameController
     */
    private Optional<GameController> restoreGame(){
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            sendAll(new TextMsg("An unfinished game has been found. Reloading..."));
            Server.logger.info("Reloading [" + Server.formatGameName(players.keySet()) + "] game");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Optional<GameController> game = Optional.of((GameController) ois.readObject());
            ois.close();
            return game;
        }
        catch (FileNotFoundException e){
            Server.logger.info("Creating save file for [" + Server.formatGameName(players.keySet()) + "]");
            return Optional.empty();
        }
        catch (IOException | ClassNotFoundException e){
            sendAll(new TextMsg("Error reloading previous game. Starting a new one."));
            Server.logger.info("Error reloading save file for [" + Server.formatGameName(players.keySet()) +"] Creating new one.");
            return Optional.empty();
        }
    }

    /**
     * Saves the current state of the GameController in the saves folder
     */
    private void saveGameState(){
        try {
            if (!saveDir.exists() && !saveDir.mkdir())
                throw new IOException();
            if (saveFile.exists() && !saveFile.delete())
                throw new IOException();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
            oos.writeObject(gameController);
            oos.close();
        }
        catch (IOException e){
            Server.logger.error("Could not update save file.");
        }
    }

    /**
     * Keeps listening for the current player's ClientHandler and whenever a message is read it is executed
     */
    @Override
    public void run() {
        while(!exiting){
            try {
                Object nextMsg = players.get(getPlayingUsername()).readObject();
                CommandMsg command = (CommandMsg)nextMsg;
                Server.logger.debug(command);
                command.execute(gameController);
                saveGameState();
            } catch (IOException | ClassNotFoundException e) {
                Server.logger.warn("Connection dropped with player " + getPlayingUsername() + " [" + players.get(getPlayingUsername()).getIP() + "]");
                exitGame();
            }
        }
    }

    /**
     * Notifies the players that a new player has started his turn
     */
    public void notifyNewTurn(){
        if (turnJustFinished == null || turnJustFinished){
            messageFilter(null, getPlayingUsername() + " is playing");
            turnJustFinished = false;
        }
    }

    /**
     * Notifies the player that the solo rival is playing its turn
     */
    public void notifyRivalTurn(){
        if (!gameController.isSinglePlayer())
            throw new IllegalStateException();

        sendPlaying(new TextMsg("Lorenzo il Magnifico is playing"));
    }

    /**
     * Requests the current player to choose a leader card to discard
     */
    public void requestDiscardLeaderCard(){
        sendOthers(new TextMsg("Waiting for " + getPlayingUsername() + " to choose his leader cards", true));
        sendPlaying(new DiscardLeaderRequestMsg());
    }

    /**
     * Requests the current player to select an action
     * @param isPostTurn    If true, the game expects to receive a normal action or a leader card action.
     *                      If false, the game expects to receive an end turn command or a leader card action
     */
    public void nextAction(boolean isPostTurn){
        sendPlaying(new NextActionMsg(isPostTurn));
    }

    /**
     * Requests the current player to place or discard a marble in the MarbleBuffer
     */
    public void manageResource(){
        sendPlaying(new ManageResourceMsg());
    }

    /**
     * Requests all clients to remove a LeaderCard from the current player
     * @param cardId    The card ID
     */
    public void discardLeaderCardUpdate(int cardId){

        sendPlaying(new DiscardLeaderCardUpdateMsg(getPlayingUsername(), cardId));
        messageFilter(null, getPlayingUsername() + " has discarded a leader card");
    }

    /**
     * Requests the current client to create a MarbleBuffer
     * @param marbleBuffer  The content of the buffer
     */
    public void createBuffer(List<Marble> marbleBuffer){
        sendPlaying(new CreateBufferMsg(marbleBuffer));
    }

    /**
     * Requests the current player to put a resource in a depot
     */
    public void requestPutResource(){
        sendOthers(new TextMsg("Waiting for " + getPlayingUsername() + " to choose his resources", true));
        sendPlaying(new PutResourceRequestMsg());
    }

    /**
     * Requests the current client to remove a marble from its MarbleBuffer
     * @param marble    The removed marble
     */
    public void bufferUpdate(Marble marble){
        sendPlaying(new BufferUpdateMsg(marble));
    }

    /**
     * Requests all clients to add an extra ProductionPower to the current player
     * @param power     The extra production power
     */
    public void extraPowerUpdate(ProductionPower power){
        sendAll(new ExtraPowerUpdateMsg(getPlayingUsername(), power));
    }

    /**
     * Requests all clients to update the content of their FaithTrack
     */
    public void faithTrackUpdate(){

        Map<String, Integer> positions = new HashMap<>();
        for (AbstractPlayer p : gameController.getFaithTrack().getPlayers())
            positions.put(p.getUsername(), p.getPosition().getNumber());

        if (turnJustFinished && gameController.isSinglePlayer()){
            sendPlaying(new TrackUpdateMsg(positions));
            sendPlaying(new TextMsg("Lorenzo il Magnifico advanced on the faith track"));
        } else
            messageFilter(new TrackUpdateMsg(positions), getPlayingUsername() + " has changed the faith track");
    }

    /**
     * Requests all the clients to set a certain player's tile as gained
     * @param username  The player username
     * @param tileIdx   The index of the gained tile
     */
    public void tileGainedUpdate(String username, int tileIdx){

        messageFilter(new TileGainedUpdateMsg(username, tileIdx), username + " has gained a pope favour tile");
    }

    /**
     * Requests all the clients to update the content of the current player's warehouse
     */
    public void warehouseUpdate() {

        messageFilter(new WarehouseUpdateMsg(gameController.getPlaying().getPlayerBoard().getWareHouse().getSimple(),
                getPlayingUsername()), getPlayingUsername() + " has changed his warehouse");
    }

    /**
     * Requests all the clients to update the content of the current player's strongbox
     */
    public void strongBoxUpdate() {

        Map<Resource, Integer> strongbox = gameController.getPlaying().getPlayerBoard().getStrongBox().getContent();
        UpdateMsg msg = new StrongBoxUpdateMsg(strongbox, getPlayingUsername());
        messageFilter(msg, getPlayingUsername() + " has changed his strongbox");
    }

    /**
     * Requests all the clients to update the content of the MarketBoard
     */
    public void marketBoardUpdate(){

        messageFilter(new MarketBoardUpdate(gameController.getMarketBoard().getMarbleGrid(),
                gameController.getMarketBoard().getSpareMarble()), getPlayingUsername() + " has changed the marketboard");
    }

    /**
     * Requests all the clients to add a card in the current player's PlayerBoard
     * @param cardId    The card ID
     * @param slotIdx   The slot position
     */
    public void addCardInSlotUpdate(int cardId, int slotIdx){

        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(getPlayingUsername(), cardId, slotIdx);
        messageFilter(msg, getPlayingUsername() + " has bought a development card");
    }

    /**
     * Requests all the clients to remove a card from the top of a deck
     * @param cardcolor     The deck's color
     * @param level         The deck's level
     */
    public void devCardDecksUpdate(CardColor cardcolor, int level){

        if (turnJustFinished && gameController.isSinglePlayer()) {
            sendPlaying(new CardDecksUpdateMsg(level, cardcolor));
            sendPlaying(new TextMsg("Lorenzo il Magnifico has drawn a level " + level + " " + cardcolor.toString().toLowerCase() + " " + "card"));
        }else
            sendAll(new CardDecksUpdateMsg(level, cardcolor));
    }

    /**
     * Requests all the clients to set a current player's card as active.
     * It the player doesn't correspond to the client, the card is now visible
     * @param cardId    The card ID
     */
    public void playLeaderCardUpdate(int cardId) {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, getPlayingUsername() + " has played a leader card");
    }

    /**
     * Requests all the clients to add a set of white marble aliases to a player
     * @param username  The player username
     * @param aliases   The Set of white marble aliases
     */
    public void whiteMarbleAliasUpdate(String username, Set<Resource> aliases){
        sendAll(new WhiteMarbleAliasUpdateMsg(username, aliases));
    }

    /**
     * Requests all the clients to add a card discount ot a player
     * @param username  The player username
     * @param resource  The discount resource
     */
    public void cardDiscountUpdate(String username, Resource resource){
        sendAll(new CardDiscountUpdateMsg(username, resource));
    }

    /**
     * Notifies all clients of the end of the initialization phase
     */
    public void endInit(){
        sendAll(new EndInitMsg());
    }

    /**
     * Asks all the clients to end the game, sending the leaderboard, and exits.
     */
    public void endGame(){
        if (gameController.isSinglePlayer())
            throw new IllegalStateException();
        sendAll(new EndGameMsg(gameController.getLeaderboard()));
        exitGame();
    }

    /**
     * Asks the single player client to end the game, telling him if he won and how many points he did, then exits.
     */
    public void endSinglePlayerGame(){

        if (!gameController.isSinglePlayer() || gameController.getSoloRival().isEmpty())
            throw new IllegalStateException();
        boolean win = (gameController.getPlaying().getPosition().getNumber() == FaithTrack.LAST_POSITION)
                || (gameController.getPlaying().countDevCards() == 7);

        sendAll(new EndGameMsg(gameController.getLeaderboard(), win));
        exitGame();
    }

    /**
     * Shuts down all connections, logs out usernames, deletes the save file.
     */
    public void exitGame() {

        Server.logger.info("Exiting game [" + Server.formatGameName(players.keySet()) + "]");
        exiting = true;
        for (String username : players.keySet()) {
            Server.logOut(username);
            players.get(username).closeConnection();
        }
        while (saveFile.exists()){
            if (!saveFile.delete())
                Server.logger.error("Error deleting save file for [" + Server.formatGameName(players.keySet()) + "]");
            else {
                Server.logger.info("Deleted save file for [" + Server.formatGameName(players.keySet()) + "]");
                break;
            }
        }
    }

    /**
     * Sends a message to all players, and a log message to those that aren't playing
     * @param msg   The message to send to all players
     * @param text  The log message
     */
    public void messageFilter(Msg msg, String text) {
        sendOthers(new TextMsg(text));
        if (msg != null)
            sendAll(msg);
    }

    private void sendOthers(Msg msg){
        Set<String> others = new HashSet<>(players.keySet());
        others.remove(getPlayingUsername());
        for (String other : others)
            sendPlayer(other, msg);
    }

    private void sendPlayer(String player, Msg msg){
        try {
            players.get(player).writeObject(msg);
        }
        catch (IOException e){
            Server.logger.warn("Connection dropped with player " + getPlayingUsername() + " [" + players.get(getPlayingUsername()).getIP() + "]");
            exitGame();
        }
    }

    private void sendAll(Msg msg) {
        for (String username : players.keySet())
            sendPlayer(username, msg);
    }

    private void sendPlaying(Msg msg) {
        sendPlayer(getPlayingUsername(), msg);
    }

    /**
     * Sends ad error message to the current player
     * @param text  The error text
     */
    public void sendError (String text){
        sendPlaying(new ErrorMsg(text));
    }

    private String getPlayingUsername(){
        return gameController.getPlaying().getUsername();
    }

    public void setTurnJustFinished(Boolean turnJustFinished) {
        this.turnJustFinished = turnJustFinished;
    }

    public GameController getGameController(){
        return gameController;
    }
}