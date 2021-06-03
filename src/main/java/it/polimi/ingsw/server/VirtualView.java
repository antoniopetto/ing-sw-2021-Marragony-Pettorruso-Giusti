package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Msg;
import it.polimi.ingsw.messages.toview.*;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.FaithTrack;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.update.*;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;
import java.util.*;

public class VirtualView implements Runnable{

    private boolean exiting = false;
    private final GameController gameController;
    private final Map<String, ClientHandler> players = new HashMap<>();

    public VirtualView(Map<String, ClientHandler> players){
        this.players.putAll(players);
        gameController = new GameController(this.players.keySet(), this);

        for (String playerName : players.keySet())
            sendPlayer(playerName, new InitModelMsg(gameController.getSimple(playerName)));

        sendAll(new TitleMsg());
        requestDiscardLeaderCard();
    }

    @Override
    public void run() {
        while(!exiting){
            try {
                Object nextMsg = players.get(getPlayingUsername()).readObject();
                CommandMsg command = (CommandMsg)nextMsg;
                System.out.println(command);
                command.execute(gameController);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                exitGame();
            }
        }
    }

    public void requestDiscardLeaderCard(){
        sendPlaying(new DiscardLeaderRequestMsg());
    }

    public void startPlay(){
        messageFilter(new TurnMsg(false), getPlayingUsername() + " is playing the turn...");
    }

    public void nextAction(boolean isPostTurn){
        sendPlaying(new TurnMsg(isPostTurn));
    }

    public void manageResource(){
        sendPlaying(new ManageResourceMsg());
    }

    public void discardLeaderCardUpdate(int cardId){
        DiscardLeaderCardUpdateMsg msg = new DiscardLeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, getPlayingUsername() + " has discarded a leader card");
    }

    public void createBuffer(List<Marble> marbleBuffer){
        sendAll(new CreateBufferMsg(marbleBuffer));
    }

    public void requestPutResource(){
        sendPlaying(new PutResourceRequestMsg());
    }

    public void bufferUpdate(Marble marble){
        sendAll(new BufferUpdateMsg(marble));
    }

    public void extraPowerUpdate(ProductionPower power){
        sendAll(new ExtraPowerUpdateMsg(getPlayingUsername(), power));
    }

    public void faithTrackUpdate(){

        Map<String, Integer> positions = new HashMap<>();
        for (AbstractPlayer p : gameController.getFaithTrack().getPlayers())
            positions.put(p.getUsername(), p.getPosition().getNumber());
        UpdateMsg msg = new TrackUpdateMsg(positions);
        sendAll(msg);
    }

    public void vaticanReportUpdate(){}

    public void warehouseUpdate() {
        sendAll(new WarehouseUpdateMsg(gameController.getPlaying().getPlayerBoard().getWareHouse().getSimple(), getPlayingUsername()));
    }

    public void strongBoxUpdate() {
        Map<Resource, Integer> strongbox = gameController.getPlaying().getPlayerBoard().getStrongBox().getContent();
        UpdateMsg msg = new StrongBoxUpdateMsg(strongbox, getPlayingUsername());
        sendAll(msg);
    }

    public void marketBoardUpdate(){
        sendAll(new MarketBoardUpdate(gameController.getMarketBoard().getMarbleGrid(),
                                      gameController.getMarketBoard().getSpareMarble()));
    }

    public void addCardInSlotUpdate(int cardId, int slotIdx){
        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(getPlayingUsername(), cardId, slotIdx);
        messageFilter(msg, getPlayingUsername() + "has bought a development card");
    }

    public void devCardDecksUpdate(CardColor cardcolor, int level){
        UpdateMsg msg = new CardDecksUpdateMsg(level, cardcolor);
        sendAll(msg);
    }

    public void playLeaderCardUpdate(int cardId) {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, getPlayingUsername() + "has played a LeaderCard");
    }

    public void whiteMarbleAliasUpdate(String username, Set<Resource> aliases){
        WhiteMarbleAliasUpdateMsg msg = new WhiteMarbleAliasUpdateMsg(username, aliases);
        sendAll(msg);
    }

    public void endGame(){
        if (gameController.isSinglePlayer())
            throw new IllegalStateException();
        sendAll(new EndGameMsg(gameController.getLeaderboard()));
        exitGame();
    }

    public void endSinglePlayerGame(){

        if (!gameController.isSinglePlayer() || gameController.getSoloRival() == null)
            throw new IllegalStateException();
        boolean win = (gameController.getPlaying().getPosition().getNumber() == FaithTrack.LAST_POSITION)
                || (gameController.getPlaying().countDevCards() == 7);

        sendAll(new EndGameMsg(gameController.getLeaderboard(), win));
        exitGame();
    }

    public void exitGame(){
        System.out.println("Exiting game");
        exiting = true;
        for (String username : players.keySet()){
            Server.logOut(username);
            players.get(username).closeConnection();
        }
        //TODO here delete file
    }

    public void messageFilter(Msg msg, String text) {
        Set<String> others = new HashSet<>(players.keySet());
        others.remove(getPlayingUsername());
        for (String other : others)
            sendPlayer(other, new TextMsg(text));
        if (msg != null)
            sendPlaying(msg);
    }

    private void sendPlayer(String player, Msg msg){
        try {
            players.get(player).writeObject(msg);
        }
        catch (IOException e){
            System.out.println("Connection dropped");
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
}