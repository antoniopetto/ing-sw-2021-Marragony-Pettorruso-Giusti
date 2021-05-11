package it.polimi.ingsw.server;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.messages.update.InitChoicesMsg;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.LeaderCard;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.update.*;
import it.polimi.ingsw.messages.view.ErrorMsg;
import it.polimi.ingsw.messages.view.LeaderboardMsg;
import it.polimi.ingsw.messages.command.CommandMsg;
import it.polimi.ingsw.messages.view.NewTurnMessage;

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
                command.execute(game, getPlayingHandler());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                exitGame();
            }
        }
    }

    private void initGame()
    {
        List<SimplePlayer> simplePlayerList = game.initializePlayers();
        int[][][] cardIDs = game.getDevelopmentCardDecks().getDecksStatus();
        for (Map.Entry<String, ClientHandler> map: players.entrySet()) {
            try {
                UpdateMsg msg = new GameInitMsg(simplePlayerList, cardIDs, map.getKey());
                map.getValue().writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initChoices();
        marketBoardUpdate();
    }

    public void initChoices(){
        try {
            InitChoicesMsg msg = new InitChoicesMsg();
            getPlayingHandler().writeObject(msg);
        } catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }

    //NEW
    public void startPlay(){
        try{
            getPlayingHandler().writeObject(new NewTurnMessage());

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

    public void bufferUpdate(Marble marble){
        try {
            BufferUpdateMsg msg = new BufferUpdateMsg(marble);
            getPlayingHandler().writeObject(msg);
        } catch (IOException e){
            e.printStackTrace();
            exitGame();
        }
    }






    public void faithTrackUpdate(AbstractPlayer player, boolean allBut){
        UpdateMsg msg = new TrackUpdateMsg(player, allBut);
        sendAll(msg);
    }

    public void vaticanReportUpdate(){}

    public void warehouseUpdate()
    {
        Map<DepotName, Map<Resource, Integer>> warehouse = new HashMap<>();
        for (Depot depot: game.getPlaying().getPlayerBoard().getWareHouse().getDepots()) {
            Map<Resource, Integer> resources = new HashMap<>();
            resources.put(depot.getResource(), depot.getQuantity());
            warehouse.put(depot.getName(), resources);
        }

        UpdateMsg msg = new WarehouseUpdateMsg(warehouse, getPlayingUsername());
        sendAll(msg);

    }


    public void marketBoardUpdate(){
        UpdateMsg msg = new MarketBoardUpdate(game.getMarketBoard().getMarbleGrid(), game.getMarketBoard().getSpareMarble());
        sendAll(msg);
    }

    public void addCardInSlotUpdate(int cardId, int slotId){
        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(game.getPlaying().getUsername(), cardId, slotId);
        messageFilter(msg, "The player '"+ game.getPlaying().getUsername()+ "' has bought a development card");
    }

    public void devcarddecksUpdate(int level, int cardColor,  int cardTop){
        UpdateMsg msg = new CardDecksUpdateMsg(level,cardColor,cardTop);
        sendAll(msg);
    }

    public void playLeaderCardUpdate(int cardId) {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(game.getPlaying().getUsername(), cardId);
        messageFilter(msg, "The player '"+ game.getPlaying().getUsername()+ "' plays a LeaderCard");
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


    private void messageFilter(UpdateMsg msg, String text){

        for (Map.Entry<String, ClientHandler> map: players.entrySet()) {
            try {
                if(!map.getKey().equals(game.getPlaying().getUsername())){
                    ErrorMsg errorMsg = new ErrorMsg(text);
                    map.getValue().writeObject(errorMsg);
                }
                map.getValue().writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendAll(UpdateMsg msg)
    {
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
