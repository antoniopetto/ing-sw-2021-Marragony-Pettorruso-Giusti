package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.simplemodel.SimpleWarehouse;

public class WarehouseUpdateMsg implements UpdateMsg {

    private final SimpleWarehouse warehouse;
    private final String username;

    public WarehouseUpdateMsg(SimpleWarehouse warehouse, String username) {
        this.warehouse = warehouse;
        this.username = username;
    }

    @Override
    public void execute(SimpleModel game) {
        for (SimplePlayer player : game.getPlayers())
            if (player.getUsername().equals(username))
                player.setWarehouse(warehouse);
    }

    @Override
    public String toString() {
        return "WarehouseUpdateMsg{" +
                "warehouse=" + warehouse +
                ", username='" + username + '\'' +
                '}';
    }
}
