package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

import java.util.Arrays;
import java.util.Set;

public class InitCardsUpdateMsg implements UpdateMsg{

    private final int[][][] devCardIds;
    private final Set<Integer> leaderCardIds;

    public InitCardsUpdateMsg(int[][][] devCardIds, Set<Integer> leaderCardIds){
        this.devCardIds = devCardIds;
        this.leaderCardIds = leaderCardIds;
    }

    @Override
    public void execute(SimpleGame game) {
        game.initCards(devCardIds, leaderCardIds);
    }

    @Override
    public String toString() {
        return "InitCardsUpdateMsg{" +
                ", cardIDs=" + Arrays.toString(devCardIds) +
                '}';
    }
}
