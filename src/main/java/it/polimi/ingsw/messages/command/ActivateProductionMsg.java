package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.ProductionPower;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ActivateProductionMsg implements CommandMsg{

    private final Set<Integer> selectedCardIds;
    private final Map<Integer, ProductionPower> selectedExtraPowers;

    public ActivateProductionMsg(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        this.selectedCardIds = new HashSet<>(selectedCardIds);
        this.selectedExtraPowers = new HashMap<>(selectedExtraPowers);
    }

    public void execute(Game game){
        game.activateProduction(selectedCardIds, selectedExtraPowers);
    }

    @Override
    public String toString() {
        return "ActivateProductionMsg{" +
                "selectedCardIds=" + selectedCardIds +
                ", selectedExtraPowers=" + selectedExtraPowers +
                '}';
    }
}
