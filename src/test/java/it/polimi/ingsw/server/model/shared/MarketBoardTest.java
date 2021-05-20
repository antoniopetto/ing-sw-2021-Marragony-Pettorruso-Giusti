package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import org.junit.Test;
import org.mockito.Mockito;

import static it.polimi.ingsw.server.model.shared.MarketBoard.COLUMNS;
import static it.polimi.ingsw.server.model.shared.MarketBoard.ROWS;
import static org.junit.Assert.assertEquals;

public class MarketBoardTest {

    private final MarketBoard marketBoard = new MarketBoard(Mockito.mock(VirtualView.class));

    @Test
    public void buyColumnTest() {

        Marble[][] preGrid = marketBoard.getMarbleGrid();
        Marble preSpare = marketBoard.getSpareMarble();
        marketBoard.buyColumn(2);
        Marble[][] postGrid = marketBoard.getMarbleGrid();
        Marble postSpare = marketBoard.getSpareMarble();

        assertEquals(preGrid[0][2], postSpare);
        assertEquals(preSpare, postGrid[ROWS-1][2]);
        for(int i = 1; i < ROWS - 1; i++){
            assertEquals(preGrid[i][2], postGrid[i-1][2]);
        }
    }

    @Test
    public void insertSpareInRowTest() {

        Marble[][] preGrid = marketBoard.getMarbleGrid();
        Marble preSpare = marketBoard.getSpareMarble();
        marketBoard.buyRow(2);
        Marble[][] postGrid = marketBoard.getMarbleGrid();
        Marble postSpare = marketBoard.getSpareMarble();

        assertEquals(preGrid[2][0], postSpare);
        assertEquals(preSpare, postGrid[2][COLUMNS - 1]);
        for (int i = 1; i < COLUMNS - 1; i++) {
            assertEquals(preGrid[2][i], postGrid[2][i-1]);
        }
    }
}