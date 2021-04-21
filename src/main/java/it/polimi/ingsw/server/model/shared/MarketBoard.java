package it.polimi.ingsw.server.model.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the marble market.
 * It holds the 3x4 grid of <a href="#{@link}">{@link Marble}</a> from which a player can buy a column or row,
 * and provides methods for retrieving its content, and for inserting the spare <code>Marble</code> in the desired place.
 *
 * @see Marble
 */
public class MarketBoard {

    private static final int ROWS = 3;
    private static final int COLUMNS = 4;
    private final Marble[][] marbleGrid = new Marble[ROWS][COLUMNS];
    private Marble spareMarble;

    /**
     * Constructs the MarketBoard filling it with the 13 default game marbles.
     */
    public MarketBoard() {

        List<Marble> marbles = new ArrayList<>();
        marbles.addAll(Collections.nCopies(1, Marble.RED));
        marbles.addAll(Collections.nCopies(2, Marble.BLUE));
        marbles.addAll(Collections.nCopies(2, Marble.YELLOW));
        marbles.addAll(Collections.nCopies(2, Marble.GREY));
        marbles.addAll(Collections.nCopies(2, Marble.PURPLE));
        marbles.addAll(Collections.nCopies(4, Marble.WHITE));
        Collections.shuffle(marbles);

        for (int i = 0; i < marbles.size() - 1; i++)
            marbleGrid[i / COLUMNS][i % COLUMNS] = marbles.get(i);

        spareMarble = marbles.get(marbles.size() - 1);
    }

    /**
     * Constructs the MarketBoard restoring a previous game.
     *
     * @param marbles                       The list of input <code>Marble</code>,
     *                                      ordered row by row, the 13th being the spare one.
     * @throws IllegalArgumentException     If <code>marbles</code> doesn't contain exactly 13 elements.
     */
    public MarketBoard(List<Marble> marbles) {

        if (marbles.size() != ROWS * COLUMNS + 1)
            throw new IllegalArgumentException("MarketBoard must contain" + ROWS * COLUMNS + 1 + "marbles");

        for (int i = 0; i < marbles.size() - 1; i++)
            marbleGrid[i / COLUMNS][i % COLUMNS] = marbles.get(i);

        spareMarble = marbles.get(marbles.size() - 1);
    }

    /**
     * Inserts the spare <code>Marble</code> at the end of the input column,
     * shifting the rest and storing the new spare.
     *
     * @param columnId  The column in which the spare marble gets inserted.
     */
    public void insertSpareInColumn(int columnId) {

        Marble tmp = spareMarble;
        spareMarble = marbleGrid[0][columnId];

        for (int i = 0; i < ROWS - 1; i++)
            marbleGrid[i][columnId] = marbleGrid[i + 1][columnId];

        marbleGrid[ROWS - 1][columnId] = tmp;
    }

    /**
     * Inserts the spare <code>Marble</code> at the end of the input column,
     * shifting the rest and storing the new spare.
     *
     * @param rowId   The row in which the spare marble gets inserted.
     */
    public void insertSpareInRow(int rowId) {

        Marble tmp = spareMarble;
        spareMarble = marbleGrid[rowId][0];
        Collections.rotate(Arrays.asList(marbleGrid[rowId]),-1);
        marbleGrid[rowId][COLUMNS - 1] = tmp;
    }

    public Marble getSpareMarble() { return spareMarble; }

    public List<Marble> getColumn(int columnId) {

        List<Marble> column = new ArrayList<>();

        for (int i = 0; i < ROWS; i++){
            column.add(marbleGrid[i][columnId]);
        }
        return column;
    }

    public List<Marble> getRow(int rowId) { return Arrays.asList(marbleGrid[rowId].clone()); }

}