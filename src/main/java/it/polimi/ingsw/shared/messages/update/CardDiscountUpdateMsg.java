package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.Resource;

import java.io.Serializable;

public class CardDiscountUpdateMsg implements UpdateMsg, Serializable {

    private final Resource resource;
    private final String username;

    public CardDiscountUpdateMsg(String username, Resource resource) {
        this.resource = resource;
        this.username = username;
    }

    @Override
    public void execute(SimpleModel game) {

        game.getPlayers().stream()
                .filter(p -> p.getUsername().equals(username)).findAny()
                .ifPresent(p -> p.addCardDiscount(resource));
    }
}
