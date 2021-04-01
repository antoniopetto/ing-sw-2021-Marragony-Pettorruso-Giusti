package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MarketBoardTest {

    private final List<Marble> parsedList = new ArrayList<>();
    private MarketBoard marketBoard;

    @Before
    public void setUp() {
        // Generate the MarketBoard with hardcoded Marbles
        parsedList.clear();
        parsedList.addAll(Collections.nCopies(1, Marble.RED));
        parsedList.addAll(Collections.nCopies(2, Marble.BLUE));
        parsedList.addAll(Collections.nCopies(2, Marble.YELLOW));
        parsedList.addAll(Collections.nCopies(2, Marble.GREY));
        parsedList.addAll(Collections.nCopies(2, Marble.PURPLE));
        parsedList.addAll(Collections.nCopies(4, Marble.WHITE));
        marketBoard = new MarketBoard(parsedList);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ConstructorTest() {
        // Tries to initialize another MarketBoard with the wrong number of input marbles
        parsedList.remove(0);
        marketBoard = new MarketBoard(parsedList);
    }

    @Test
    public void insertSpareInColumnTest() {
        // Checks correctness of the shift operation on a certain column
        int columnId = 2;
        List<Marble> preColumn = marketBoard.getColumn(columnId);
        Marble preSpareMarble = marketBoard.getSpareMarble();
        int size = preColumn.size();

        marketBoard.insertSpareInColumn(columnId);
        List<Marble> postColumn = marketBoard.getColumn(columnId);

        assertEquals(preColumn.size(), postColumn.size());
        assertEquals(preSpareMarble, postColumn.get(size - 1));
        assertEquals(preColumn.get(0), marketBoard.getSpareMarble());

        for (int i = 0; i < size - 1; i++)
            assertEquals("Shift column error at position " + i,
                    postColumn.get(i),
                    preColumn.get(i + 1));
    }

    @Test
    public void insertSpareInRowTest() {
        // Checks correctness of the shift operation on a certain row
        int rowId = 2;
        List<Marble> preRow = marketBoard.getRow(rowId);
        Marble preSpareMarble = marketBoard.getSpareMarble();

        marketBoard.insertSpareInRow(rowId);
        List<Marble> postRow = marketBoard.getRow(rowId);

        assertEquals(preRow.size(), postRow.size());
        assertEquals(preSpareMarble, postRow.get(preRow.size() - 1));
        assertEquals(preRow.get(0), marketBoard.getSpareMarble());

        for (int i = 0; i < preRow.size() - 1; i++)
            assertEquals("Shift row error at position " + i,
                    postRow.get(i),
                    preRow.get(i+1));
    }
}