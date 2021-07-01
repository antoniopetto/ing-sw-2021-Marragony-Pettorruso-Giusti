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
import java.util.*;

public class VirtualView implements Runnable{

    private static final String SAVES_PATH = "src/main/resources/saves/";
    private static final File saveDir = new File(SAVES_PATH);

    private final File saveFile;
    private boolean exiting = false;
    private final GameController gameController;
    private final Map<String, ClientHandler> players = new HashMap<>();
    private Boolean turnJustFinished = null;

    public VirtualView(Map<String, ClientHandler> players){

        this.players.putAll(players);
        saveFile = new File(SAVES_PATH + Server.formatGameName(players.keySet()) + ".ser");

        gameController = restoreGame()
                .orElse(new GameController(this.players.keySet()));
        gameController.setVirtualView(this);

        for (String playerName : players.keySet())
            sendPlayer(playerName, new InitModelMsg(gameController.getSimple(playerName)));

        sendAll(new TitleMsg());
        gameController.resumeGame();
    }

    private Optional<GameController> restoreGame(){
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            sendAll(new TextMsg("An unfinished game has been found. Reloading..."));
            ObjectInputStream ois = new ObjectInputStream(fis);
            return Optional.of((GameController) ois.readObject());
        }
        catch (FileNotFoundException e){
            return Optional.empty();
        }
        catch (IOException | ClassNotFoundException e){
            sendAll(new TextMsg("Error reloading previous game. Starting a new one."));
            return Optional.empty();
        }
    }

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

    public void notifyNewTurn(){
        if (turnJustFinished == null || turnJustFinished){
            messageFilter(null, getPlayingUsername() + " is playing");
            turnJustFinished = false;
        }
    }

    public void notifyRivalTurn(){
        if (!gameController.isSinglePlayer())
            throw new IllegalStateException();

        sendPlaying(new TextMsg("Lorenzo il Magnifico is playing"));
    }

    public void requestDiscardLeaderCard(){
        sendOthers(new TextMsg("Waiting for " + getPlayingUsername() + " to choose his leader cards", true));
        sendPlaying(new DiscardLeaderRequestMsg());
    }

    public void nextAction(boolean isPostTurn){
        sendPlaying(new NextActionMsg(isPostTurn));
    }

    public void manageResource(){
        sendPlaying(new ManageResourceMsg());
    }

    public void discardLeaderCardUpdate(int cardId){

        sendPlaying(new DiscardLeaderCardUpdateMsg(getPlayingUsername(), cardId));
        messageFilter(null, getPlayingUsername() + " has discarded a leader card");
    }

    public void createBuffer(List<Marble> marbleBuffer){
        sendPlaying(new CreateBufferMsg(marbleBuffer));
    }

    public void requestPutResource(){
        sendOthers(new TextMsg("Waiting for " + getPlayingUsername() + " to choose his resources", true));
        sendPlaying(new PutResourceRequestMsg());
    }

    public void bufferUpdate(Marble marble){
        sendPlaying(new BufferUpdateMsg(marble));
    }

    public void extraPowerUpdate(ProductionPower power){
        sendAll(new ExtraPowerUpdateMsg(getPlayingUsername(), power));
    }

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

    public void tileGainedUpdate(String username, int tileIdx){

        messageFilter(new TileGainedUpdateMsg(username, tileIdx), username + " has gained a pope favour tile");
    }

    public void warehouseUpdate() {

        messageFilter(new WarehouseUpdateMsg(gameController.getPlaying().getPlayerBoard().getWareHouse().getSimple(),
                getPlayingUsername()), getPlayingUsername() + " has changed his warehouse");
    }

    public void strongBoxUpdate() {

        Map<Resource, Integer> strongbox = gameController.getPlaying().getPlayerBoard().getStrongBox().getContent();
        UpdateMsg msg = new StrongBoxUpdateMsg(strongbox, getPlayingUsername());
        messageFilter(msg, getPlayingUsername() + " has changed his strongbox");
    }

    public void marketBoardUpdate(){

        messageFilter(new MarketBoardUpdate(gameController.getMarketBoard().getMarbleGrid(),
                gameController.getMarketBoard().getSpareMarble()), getPlayingUsername() + " has changed the marketboard");
    }

    public void addCardInSlotUpdate(int cardId, int slotIdx){

        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(getPlayingUsername(), cardId, slotIdx);
        messageFilter(msg, getPlayingUsername() + " has bought a development card");
    }

    public void devCardDecksUpdate(CardColor cardcolor, int level){

        if (turnJustFinished && gameController.isSinglePlayer()) {
            sendPlaying(new CardDecksUpdateMsg(level, cardcolor));
            sendPlaying(new TextMsg("Lorenzo il Magnifico has drawn a level " + level + " " + cardcolor.toString().toLowerCase() + " " + "card"));
        }else
            sendAll(new CardDecksUpdateMsg(level, cardcolor));
    }

    public void playLeaderCardUpdate(int cardId) {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, getPlayingUsername() + " has played a leader card");
    }

    public void whiteMarbleAliasUpdate(String username, Set<Resource> aliases){
        sendAll(new WhiteMarbleAliasUpdateMsg(username, aliases));
    }

    public void cardDiscountUpdate(String username, Resource resource){
        sendAll(new CardDiscountUpdateMsg(username, resource));
    }

    public void endInit(){
        sendAll(new EndInitMsg());
    }

    public void endGame(){
        if (gameController.isSinglePlayer())
            throw new IllegalStateException();
        sendAll(new EndGameMsg(gameController.getLeaderboard()));
        exitGame();
    }

    public void endSinglePlayerGame(){

        if (!gameController.isSinglePlayer() || gameController.getSoloRival().isEmpty())
            throw new IllegalStateException();
        boolean win = (gameController.getPlaying().getPosition().getNumber() == FaithTrack.LAST_POSITION)
                || (gameController.getPlaying().countDevCards() == 7);

        sendAll(new EndGameMsg(gameController.getLeaderboard(), win));
        exitGame();
    }

    public void exitGame(){

        Server.logger.info("Exiting game [" + Server.formatGameName(players.keySet()) + "]");
        exiting = true;
        for (String username : players.keySet()){
            Server.logOut(username);
            players.get(username).closeConnection();
        }
        if (saveFile.exists() && !saveFile.delete())
            Server.logger.error("Error deleting save file");
    }

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