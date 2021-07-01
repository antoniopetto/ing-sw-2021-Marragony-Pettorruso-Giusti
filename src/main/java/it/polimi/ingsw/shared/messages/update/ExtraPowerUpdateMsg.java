package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.ProductionPower;

public class ExtraPowerUpdateMsg implements UpdateMsg{

    private final ProductionPower power;
    private final String username;

    public ExtraPowerUpdateMsg(String username, ProductionPower power){
        this.username = username;
        this.power = power;
    }

    @Override
    public void execute(SimpleModel game) {
        game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username)).findAny()
                .ifPresent(p -> p.addExtraProductionPower(power));
    }
}
