package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.shared.messages.LeaderboardMsg;
import it.polimi.ingsw.shared.messages.command.CommandMsg;
import it.polimi.ingsw.shared.messages.server.ErrorMsg;
import it.polimi.ingsw.shared.messages.server.TrackUpdateMsg;
import it.polimi.ingsw.shared.messages.server.WarehouseUpdateMsg;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class VirtualView implements Runnable{

    private AtomicBoolean exiting = new AtomicBoolean(false);
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
        while(!exiting.get()){
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

    private void exitGame(){
        exiting.set(true);
        for (Map.Entry<String, ClientHandler> entry : players.entrySet()){
            Server.logOut(entry.getKey());
            entry.getValue().closeConnection();
        }
    }
}
