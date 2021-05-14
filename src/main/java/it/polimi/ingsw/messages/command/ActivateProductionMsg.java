package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.ClientHandler;
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
        this.selectedCardIds = new HashSet<Integer>(selectedCardIds);
        this.selectedExtraPowers = new HashMap<>(selectedExtraPowers);
    }

    public void execute(Game game, ClientHandler clientHandler){
        game.activateProduction(selectedCardIds, selectedExtraPowers);
    }
}
