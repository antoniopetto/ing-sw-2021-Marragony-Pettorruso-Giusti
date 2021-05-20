package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;

import java.util.HashMap;
import java.util.Map;

public class TrackUpdateMsg implements UpdateMsg {
    private final Map<String, Integer> positions;

    public TrackUpdateMsg(Map<String, Integer> positions) {
        this.positions = new HashMap<>(positions);
    }

    @Override
    public void execute(SimpleGame model) {
        for (SimplePlayer simplePlayer : model.getPlayers()) {
            simplePlayer.setPosition(positions.get(simplePlayer.getUsername()));
        }
    }

    @Override
    public String toString() {
        return "TrackUpdateMsg{" +
                "positions=" + positions +
                '}';
    }
}
