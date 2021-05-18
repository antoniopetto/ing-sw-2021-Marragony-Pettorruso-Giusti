package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.cards.ProductionPower;

public class ExtraPowerUpdateMsg implements UpdateMsg{

    private final ProductionPower power;
    private final String username;

    public ExtraPowerUpdateMsg(String username, ProductionPower power){
        this.username = username;
        this.power = power;
    }

    @Override
    public void execute(SimpleGame game) {
        game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username)).findAny()
                .ifPresent(p -> p.addExtraProductionPower(power));
    }
}
