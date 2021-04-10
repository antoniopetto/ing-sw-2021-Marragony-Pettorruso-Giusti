package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CardColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SoloRival extends AbstractPlayer{
    private Stack<SoloActionToken> activeTokens=new Stack<>();
    private List<SoloActionToken> discardedTokens= new ArrayList<>();

    public void soloTurn(Game game){
        this.takeFirst().activateToken(game);
    }

    private SoloActionToken takeFirst(){
        SoloActionToken token;
        token = activeTokens.pop();
        discardedTokens.add(token);
        return token;
    }

    public SoloRival(){
        discardedTokens.add(new DiscardToken(CardColor.GREEN));
        discardedTokens.add(new DiscardToken(CardColor.YELLOW));
        discardedTokens.add(new DiscardToken(CardColor.BLUE));
        discardedTokens.add(new DiscardToken(CardColor.PURPLE));
        discardedTokens.add(new MoveToken(1));
        discardedTokens.add(new MoveToken(2));
        setStack();
    }

    public void setStack()
    {
        while (!activeTokens.isEmpty())
            discardedTokens.add(activeTokens.pop());
        Collections.shuffle(discardedTokens);
        for(SoloActionToken token:discardedTokens)
            activeTokens.push(token);
        discardedTokens.clear();
    }
}
