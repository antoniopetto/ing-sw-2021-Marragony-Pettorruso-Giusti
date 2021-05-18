package it.polimi.ingsw.server;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.messages.view.*;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.update.*;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.io.IOException;
import java.util.*;

public class VirtualView implements Runnable{

    private boolean exiting = false;
    private final Game game;
    private final Map<String, ClientHandler> players = new HashMap<>();

    public VirtualView(Map<String, ClientHandler> players) {
        this.players.putAll(players);
        if (players.size() == 1)
            game = Game.newSinglePlayerGame(players.keySet().iterator().next(), this);
        else
            game = Game.newRegularGame(new ArrayList<>(players.keySet()), this);

        initGame();
    }

    @Override
    public void run() {
        while(!exiting){
            try {
                Object nextMsg = getPlayingHandler().readObject();
                CommandMsg command = (CommandMsg)nextMsg;
                System.out.println(command);
                command.execute(game);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                exitGame();
            }
        }
    }

    private void initGame() {
        List<SimplePlayer> simplePlayerList = game.getSimplePlayers();
        int[][][] cardIDs = game.getDevelopmentCardDecks().getDecksStatus();
        for (SimplePlayer player : simplePlayerList){
            try{
                UpdateMsg msg = new GameInitMsg(simplePlayerList, cardIDs, player);
                players.get(player.getUsername()).writeObject(msg);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        requestDiscardLeaderCard();
        marketBoardUpdate();
    }

    public void requestDiscardLeaderCard(){
        try {
            DiscardLeaderRequestMsg msg = new DiscardLeaderRequestMsg();
            getPlayingHandler().writeObject(msg);
        } catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }


    public void startPlay(){
        try{
            getPlayingHandler().writeObject(new TurnMsg(false));
            for (String player: players.keySet()) {
                if(!player.equals(getPlayingUsername()))
                    players.get(player).writeObject(new TextMsg(getPlayingUsername()+ " is playing the turn..."));
            }
        }catch (IOException e){
            System.out.println("Connection dropped");
            exitGame();
        }
    }


    public void nextAction(boolean isPostTurn){
        try{
            getPlayingHandler().writeObject(new TurnMsg(isPostTurn));
        }catch (IOException e){
            System.out.println("Connection dropped");
            exitGame();
        }
    }

    public void manageResource(){
        try{
            getPlayingHandler().writeObject(new ManageResourceMsg());
        }catch (IOException e){
            System.out.println("Connection dropped");
            exitGame();
        }
    }

    public void discardLeaderCardUpdate(int cardId){
        DiscardLeaderCardUpdateMsg msg = new DiscardLeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, "The player '"+ getPlayingUsername()+ "' has discarded a leader card");
    }

    public void createBuffer(List<Marble> marbleBuffer){
        try{
            CreateBufferMsg msg = new CreateBufferMsg(marbleBuffer);
            getPlayingHandler().writeObject(msg);
        }
        catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }

    public void requestPutResource(){
        try{
            PutResourceRequestMsg msg = new PutResourceRequestMsg();
            getPlayingHandler().writeObject(msg);
        }
        catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }


    public void bufferUpdate(Marble marble){
        try {
            BufferUpdateMsg msg = new BufferUpdateMsg(marble);
            getPlayingHandler().writeObject(msg);
        } catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }

    public void extraPowerUpdate(ProductionPower power){
        UpdateMsg msg = new ExtraPowerUpdateMsg(getPlayingUsername(), power);
        sendAll(msg);
    }

    public void faithTrackUpdate(AbstractPlayer player, boolean allBut){
        UpdateMsg msg = new TrackUpdateMsg(player, allBut);
        sendAll(msg);
    }

    public void vaticanReportUpdate(){}

    public void warehouseUpdate() {
        Map<DepotName, Map<Resource, Integer>> warehouse = new LinkedHashMap<>();
        for (Depot depot: game.getPlaying().getPlayerBoard().getWareHouse().getDepots()) {
            Map<Resource, Integer> resources = new HashMap<>();
            if(depot.getResource()!=null&&depot.getQuantity()!=0)
                resources.put(depot.getResource(), depot.getQuantity());
            warehouse.put(depot.getName(), resources);
        }

        UpdateMsg msg = new WarehouseUpdateMsg(warehouse, getPlayingUsername());
        sendAll(msg);
    }

    public void strongBoxUpdate() {
        Map<Resource, Integer> strongbox = game.getPlaying().getPlayerBoard().getStrongBox().getContent();
        UpdateMsg msg = new StrongBoxUpdateMsg(strongbox, getPlayingUsername());
        sendAll(msg);
    }

    public void marketBoardUpdate(){
        UpdateMsg msg = new MarketBoardUpdate(game.getMarketBoard().getMarbleGrid(), game.getMarketBoard().getSpareMarble());
        sendAll(msg);
    }

    public void addCardInSlotUpdate(int cardId, int slotId){
        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(getPlayingUsername(), cardId, slotId);
        messageFilter(msg, "The player '"+ getPlayingUsername()+ "' has bought a development card");
    }

    public void devCardDecksUpdate(int level, CardColor cardcolor, int cardTop){
        UpdateMsg msg = new CardDecksUpdateMsg(level,cardcolor,cardTop);
        sendAll(msg);
    }

    public void playLeaderCardUpdate(int cardId) {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(getPlayingUsername(), cardId);
        messageFilter(msg, "The player '"+ getPlayingUsername()+ "' plays a LeaderCard");
    }

    public void whiteMarbleAliasUpdate(String username, Set<Resource> aliases){
        WhiteMarbleAliasUpdateMsg msg = new WhiteMarbleAliasUpdateMsg(username, aliases);
        sendAll(msg);
    }

    public void endGame(){
        for (Map.Entry<String, ClientHandler> entry : players.entrySet()){
            try{
                entry.getValue().writeObject(new LeaderboardMsg());
            }
            catch (IOException e){
                e.printStackTrace();
                System.out.println("Connection dropped");
            }
        }
        exitGame();
    }

    public void exitGame(){
        System.out.println("Exiting game");
        exiting = true;
        for (Map.Entry<String, ClientHandler> entry : players.entrySet()){
            Server.logOut(entry.getKey());
            entry.getValue().closeConnection();
        }
    }

    /**Auxiliary methods */

    public void messageFilter(UpdateMsg msg, String text) {
        for (Map.Entry<String, ClientHandler> map: players.entrySet()) {
            try {
                if(!map.getKey().equals(game.getPlaying().getUsername())){
                    TextMsg textMsg = new TextMsg(text);
                    map.getValue().writeObject(textMsg);
                }
                if(msg!=null)
                    map.getValue().writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAll(UpdateMsg msg) {
        players.values().forEach(c -> {
            try {
                c.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
                exitGame();
            }
        });
    }

    public void sendError (String text){
        try {
            getPlayingHandler().writeObject(new ErrorMsg(text));
        }
        catch (IOException e){
            System.out.println("Connection dropped");
            exitGame();
        }
    }

    private ClientHandler getPlayingHandler(){
        return players.get(game.getPlaying().getUsername());
    }

    private String getPlayingUsername(){
        return game.getPlaying().getUsername();
    }

}
