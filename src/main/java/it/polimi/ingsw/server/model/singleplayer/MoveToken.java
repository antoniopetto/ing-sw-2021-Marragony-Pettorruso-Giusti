package it.polimi.ingsw.server.model.singleplayer;

import it.polimi.ingsw.server.model.Game;

/**
 * This class represents a move token. If <code>steps</code> == 2 the SoloRival position increases by two;
 * if <code>steps</code> ==1 the SoloRival position increases by one and all the tokens are shuffled and a
 * new stack of tokens is created
 */
public class MoveToken implements SoloActionToken {
    private final int steps;
    private final int id;

    public MoveToken(int steps, int id) {
        this.steps = steps;
        this.id = id;
    }

    @Override
    public void activateToken(Game game) {

        if (game.getSoloRival().isEmpty())
            throw new IllegalArgumentException("A token was activated in a game with no soloRival");

        if(steps == 1) {
            game.getTrack().advance(game.getSoloRival().get());
            game.getTrack().advance(game.getSoloRival().get());
        }
        else {
            game.getTrack().advance(game.getSoloRival().get());
            game.getSoloRival().get().resetStack();
        }
    }

    @Override
    public int getId() {
        return id;
    }
}
