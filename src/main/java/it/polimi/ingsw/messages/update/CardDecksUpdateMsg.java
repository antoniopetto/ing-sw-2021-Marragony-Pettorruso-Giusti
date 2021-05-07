package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;

public class CardDecksUpdateMsg implements UpdateMsg {

    private int level;
    private int color;
    private int cardTop;

    public CardDecksUpdateMsg(int level, int color, int cardTop) {
        this.level = level;
        this.color = color;
        this.cardTop = cardTop;
    }

    @Override
    public void execute(SimpleGame model) {
        model.updateDevCardDecks(level,color,cardTop);
    }
}
