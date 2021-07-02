package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.Marble;

import java.util.Arrays;

/**
 * Update message that sets the contents of the client's MarbleBuffer
 */
public class MarketBoardUpdate implements UpdateMsg{

    private final Marble[][] marketBoard;
    private final Marble spareMarble;

    public MarketBoardUpdate(Marble[][] marketBoard, Marble spareMarble) {
        this.marketBoard = marketBoard;
        this.spareMarble = spareMarble;
    }

    @Override
    public void execute(SimpleModel model) {
        model.setMarketBoard(marketBoard);
        model.setSpareMarble(spareMarble);
    }

    @Override
    public String toString() {
        return "MarketBoardUpdate{" +
                "marketBoard=" + Arrays.deepToString(marketBoard) +
                ", spareMarble=" + spareMarble +
                '}';
    }
}
