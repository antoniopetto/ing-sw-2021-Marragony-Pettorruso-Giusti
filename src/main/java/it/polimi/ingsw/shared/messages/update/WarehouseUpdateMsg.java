package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Map;

public class WarehouseUpdateMsg implements UpdateMsg {

    private SimpleWarehouse warehouse;
    private String username;

    public WarehouseUpdateMsg(Map<DepotName, Map<Resource, Integer>> warehouse, String username) {
        this.warehouse = new SimpleWarehouse(warehouse);
        this.username = username;
    }

    @Override
    public void execute(SimpleGame game) {
        for (SimplePlayer player : game.getPlayers())
            if (player.getUsername().equals(username))
                player.changeWarehouse(warehouse);
    }
}
