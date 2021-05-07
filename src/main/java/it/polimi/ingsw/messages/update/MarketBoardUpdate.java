package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.shared.Marble;

public class MarketBoardUpdate implements UpdateMsg{

    private final Marble[][] marketBoard;
    private final Marble spareMarble;

    public MarketBoardUpdate(Marble[][] marketBoard, Marble spareMarble) {
        this.marketBoard = marketBoard;
        this.spareMarble=spareMarble;
    }

    @Override
    public void execute(SimpleGame model) {
        model.setMarketBoard(marketBoard);
        model.setSpareMarble(spareMarble);
    }
}
