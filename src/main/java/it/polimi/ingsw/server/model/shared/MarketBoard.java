package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.shared.Marble;

import java.io.Serializable;
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
public class MarketBoard implements Serializable {

    private transient VirtualView virtualView;
    public static final int ROWS = 3;
    public static final int COLUMNS = 4;
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

        for (int i = 0; i < ROWS*COLUMNS; i++)
            marbleGrid[i / COLUMNS][i % COLUMNS] = marbles.get(i);

        spareMarble = marbles.get(marbles.size() - 1);
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Inserts the spare <code>Marble</code> at the end of the input column,
     * shifting the rest and storing the new spare.
     *
     * @param columnId  The column in which the spare marble gets inserted.
     */
    private void insertSpareInColumn(int columnId) {

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
    private void insertSpareInRow(int rowId) {

        Marble tmp = spareMarble;
        spareMarble = marbleGrid[rowId][0];
        Collections.rotate(Arrays.asList(marbleGrid[rowId]),-1);
        marbleGrid[rowId][COLUMNS - 1] = tmp;
    }

    public Marble getSpareMarble() { return spareMarble; }

    public List<Marble> buyColumn(int columnId) {

        List<Marble> column = new ArrayList<>();

        for (int i = 0; i < ROWS; i++){
            column.add(marbleGrid[i][columnId]);
        }

        insertSpareInColumn(columnId);
        virtualView.marketBoardUpdate();
        return column;
    }

    public List<Marble> buyRow(int rowId) {
        List<Marble> row = Arrays.asList(marbleGrid[rowId].clone());
        insertSpareInRow(rowId);
        virtualView.marketBoardUpdate();
        return row;
    }

    public Marble[][] getMarbleGrid(){
        Marble[][] marbleGrid = new Marble[ROWS][];
        for (int i = 0; i < ROWS; i++)
            marbleGrid[i] = this.marbleGrid[i].clone();
        return marbleGrid;
    }

}