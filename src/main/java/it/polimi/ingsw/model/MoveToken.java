package it.polimi.ingsw.model;

/**
 * This class represents a move token. If <code>steps</code> == 2 the SoloRival position increases by two;
 * if <code>steps</code> ==1 the SoloRival position increases by one and all the tokens are shuffled and a
 * new stack of tokens is created
 */
public class MoveToken implements SoloActionToken{
    private final int steps;

    public MoveToken(int steps) {
        this.steps = steps;
    }

    @Override
    public void activateToken(Game game) {
        if(steps==2)
        {
            game.getTrack().advance(game.getSoloRival().get());
            game.getTrack().advance(game.getSoloRival().get());
        }
        else
        {
            game.getTrack().advance(game.getSoloRival().get());
            game.getSoloRival().get().setStack();
        }
    }
}
