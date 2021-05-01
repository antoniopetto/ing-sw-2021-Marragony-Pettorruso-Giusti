package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.shared.messages.server.*;
import it.polimi.ingsw.shared.messages.view.ErrorMsg;
import it.polimi.ingsw.shared.messages.view.LeaderboardMsg;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

import java.io.IOException;
import java.sql.ClientInfoStatus;
import java.util.*;

public class VirtualView implements Runnable{

    private boolean exiting = false;
    private final Game game;
    private Map<String, ClientHandler> players = new HashMap<>();

    public VirtualView(Map<String, ClientHandler> players)
    {
        this.players.putAll(players);
        if (players.size() == 1)
            game = Game.newSinglePlayerGame(players.keySet().iterator().next(), this);
        else
            game = Game.newRegularGame(new ArrayList<>(players.keySet()), this);
    }

    @Override
    public void run() {
        while(exiting){
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

    public void faithTrackUpdate(AbstractPlayer player, boolean allBut){
        TrackUpdateMsg msg = new TrackUpdateMsg(player, allBut);
        for (ClientHandler handler: players.values()) {
            try {
                handler.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        WarehouseUpdateMsg msg = new WarehouseUpdateMsg(warehouse, game.getPlaying().getUsername());
        players.values().forEach(c -> {
            try {
                c.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void leaderCardUpdate(int cardId)
    {
        LeaderCardUpdateMsg msg = new LeaderCardUpdateMsg(game.getPlaying().getUsername(), cardId);
        for (Map.Entry<String, ClientHandler> map: players.entrySet()) {
            try {
                if(!map.getKey().equals(game.getPlaying().getUsername())){
                    ErrorMsg errorMsg = new ErrorMsg("The"+ game.getPlaying().getUsername()+ "Player plays a LeaderCard");
                    map.getValue().writeObject(errorMsg);
                }
                else map.getValue().writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void addAndBuyCardInSlot(int cardId, int slotId){
        AddCardInSlotUpdateMsg msg = new AddCardInSlotUpdateMsg(game.getPlaying().getUsername(), cardId, slotId);
        try{
            getPlayingHandler().writeObject(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
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

    public void endGame(){
        for (Map.Entry<String, ClientHandler> entry : players.entrySet()){
            try{
                entry.getValue().writeObject(new LeaderboardMsg());
            }
            catch (IOException e){
                e.printStackTrace();
                System.out.println("Connection dropped");
                exitGame();
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
}
