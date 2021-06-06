package it.polimi.ingsw.server.model.singleplayer;

import it.polimi.ingsw.server.GameController;

import java.io.Serializable;

/**
 * This class represents a move token. If <code>steps</code> == 2 the SoloRival position increases by two;
 * if <code>steps</code> ==1 the SoloRival position increases by one and all the tokens are shuffled and a
 * new stack of tokens is created
 */
public class MoveToken implements SoloActionToken, Serializable {
    private final int steps;
    private final int id;

    public MoveToken(int steps, int id) {
        this.steps = steps;
        this.id = id;
    }

    @Override
    public void activateToken(GameController gameController) {

        SoloRival rival = gameController.getSoloRival().orElseThrow(IllegalArgumentException::new);

        if(steps == 2) {
            gameController.advance(rival);
            gameController.advance(rival);
        }
        else {
            gameController.advance(rival);
            rival.resetStack();
        }
    }

    @Override
    public int getId() {
        return id;
    }
}
