package it.polimi.ingsw.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.server.model.cards.CardColor;

public class CardDecksUpdateMsg implements UpdateMsg {

    private final int level;
    private final CardColor cardColor;

    public CardDecksUpdateMsg(int level, CardColor cardColor) {
        this.level = level;
        this.cardColor = cardColor;
    }

    @Override
    public void execute(SimpleGame model) {
        model.updateDevCardDecks(level,cardColor);
    }

    @Override
    public String toString() {
        return "CardDecksUpdateMsg{" +
                "level=" + level +
                ", cardColor=" + cardColor +
                '}';
    }
}
