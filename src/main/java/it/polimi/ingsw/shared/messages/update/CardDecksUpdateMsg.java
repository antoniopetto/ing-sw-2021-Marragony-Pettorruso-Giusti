package it.polimi.ingsw.shared.messages.update;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.CardColor;

public class CardDecksUpdateMsg implements UpdateMsg {

    private final int level;
    private final CardColor cardColor;

    public CardDecksUpdateMsg(int level, CardColor cardColor) {
        this.level = level;
        this.cardColor = cardColor;
    }

    @Override
    public void execute(SimpleModel model) {
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
