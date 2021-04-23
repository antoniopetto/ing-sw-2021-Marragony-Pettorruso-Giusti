package it.polimi.ingsw.shared.messages.server;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class WarehouseUpdateMsg implements ServerMsg{

    private SimpleWarehouse warehouse;
    private String player;

    public WarehouseUpdateMsg(Map<DepotName, Map<Resource, Integer>> warehouse, String player) {
        this.warehouse = new SimpleWarehouse(warehouse);
        this.player=player;
    }

    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer player: model.getPlayers()) {
            if(player.getUsername().equals(this.player))
                player.changeWarehouse(this.warehouse);
            else
                player.changeOthersState(player.getUsername(), this.warehouse);
        }
    }
}
