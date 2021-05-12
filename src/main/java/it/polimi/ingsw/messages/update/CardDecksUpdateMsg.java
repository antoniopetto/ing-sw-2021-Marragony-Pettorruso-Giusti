package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.cards.CardColor;

public class CardDecksUpdateMsg implements UpdateMsg {

    private int level;
    private CardColor cardColor;
    private int cardTop;

    public CardDecksUpdateMsg(int level, CardColor cardColor, int cardTop) {
        this.level = level;
        this.cardColor = cardColor;
        this.cardTop = cardTop;
    }

    @Override
    public void execute(SimpleGame model) {
        model.updateDevCardDecks(level,cardColor,cardTop);
    }
}
