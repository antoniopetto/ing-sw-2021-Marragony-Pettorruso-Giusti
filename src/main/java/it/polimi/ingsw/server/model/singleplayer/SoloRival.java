package it.polimi.ingsw.server.model.singleplayer;

import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.singleplayer.DiscardToken;
import it.polimi.ingsw.server.model.singleplayer.MoveToken;
import it.polimi.ingsw.server.model.singleplayer.SoloActionToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * This class represents the virtual player who plays against the player in a single player game. It extends the
 * AbstractPlayer class, in which the position in the faith track is saved.
 * <code>activeTokens is a stack of tokens yet to be played</code>
 * <code>discardedTokens</code> is a list of the tokens already played
 */
public class SoloRival extends AbstractPlayer {
    private Stack<SoloActionToken> activeTokens=new Stack<>();
    private List<SoloActionToken> discardedTokens= new ArrayList<>();
    private SoloActionToken lastPlayedToken;


    public SoloRival(){
        discardedTokens.add(new DiscardToken(CardColor.GREEN, 1));
        discardedTokens.add(new DiscardToken(CardColor.YELLOW, 2));
        discardedTokens.add(new DiscardToken(CardColor.BLUE, 3));
        discardedTokens.add(new DiscardToken(CardColor.PURPLE, 4));
        discardedTokens.add(new MoveToken(1, 5));
        discardedTokens.add(new MoveToken(2, 6));
        setStack();
    }

    /**
     * This method represents the turn of the virtual player. It takes the first token from <code>activeTokens</code>
     * and activates it
     * @param game is the single player game in which the token is used
     */
    public void soloTurn(Game game){
        lastPlayedToken=this.takeFirst();
        lastPlayedToken.activateToken(game);
    }

    /**
     * This method takes the first token from <code>activeTokens</code> and put it in <code>discardedTokens</code>
     * @return the token took from <code>activeTokens</code>
     */
    private SoloActionToken takeFirst(){
        SoloActionToken token;
        token = activeTokens.pop();
        discardedTokens.add(token);
        return token;
    }

    /**
     * This method creates a new stack of active tokens. It puts all the tokens in <code>discardedTokens</code>,
     * it shuffles the collection and then it puts all the tokens in <code>activeTokens</code>
     */
    public void setStack()
    {
        while (!activeTokens.isEmpty())
            discardedTokens.add(activeTokens.pop());
        Collections.shuffle(discardedTokens);
        for(SoloActionToken token:discardedTokens)
            activeTokens.push(token);
        discardedTokens.clear();
    }

    /**
     * This method does nothing because the virtual player doesn't have a vatican report effect
     */
    @Override
    public void vaticanReportEffect(int tileNumber) {
    }

    @Override
    public void activateEndGame() {

    }

    public SoloActionToken getLastPlayedToken() {
        return lastPlayedToken;
    }
}
