package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.shared.messages.command.CommandMsg;
import it.polimi.ingsw.shared.messages.server.TrackUpdateMsg;
import it.polimi.ingsw.shared.messages.server.WarehouseUpdateMsg;

import java.io.IOException;
import java.util.*;

public class VirtualView implements Runnable{
    private final Game game;
    private Map<String, ClientHandler> players = new HashMap<>();

    public VirtualView(Map<String, ClientHandler> players)
    {
        this.players.putAll(players);
        if (players.size() == 1)
            game = Game.newSinglePlayerGame(players.keySet().iterator().next());
        else
            game = Game.newRegularGame(new ArrayList<>(players.keySet()));
    }

    @Override
    public void run() {
        while(!game.isEndgame()){
            ClientHandler handler = players.get(game.getPlaying().getUsername());
            try {
                Object nextMsg = handler.readObject();
                CommandMsg command = (CommandMsg)nextMsg;
                command.execute(game, handler);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
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
}
