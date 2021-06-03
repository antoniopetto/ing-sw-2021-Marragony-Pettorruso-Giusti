package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.client.simplemodel.SimpleSlot;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.cards.Card;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import java.util.List;
import java.util.Stack;

/**
 * This class represents the Slot.
 * It will contain a maximum of three <code>DevelopmentCards</code>
 *
 * @see DevelopmentCard
 */
public class Slot {

    private final Stack<DevelopmentCard> developmentCardList = new Stack<>();

    /**
     *
     * @return the last <code>DevelopmentCard</code> inserted in this slot
     */
    public DevelopmentCard getLastCard(){ return isEmpty() ? null : this.developmentCardList.peek(); }

    public SimpleSlot getSimple(){

        SimpleSlot slot = new SimpleSlot();
        for (DevelopmentCard card : developmentCardList)
            slot.addCard(card.getSimple());
        return slot;
    }

    /**
     *
     * @return all the <code>DevelopmentCard</code> in this slot
     */
    public List<DevelopmentCard> getDevelopmentCardList() { return developmentCardList; }

    public boolean canAddCard(DevelopmentCard dCard){ return isEmpty() ? dCard.getLevel()==1  : dCard.isLevelHigher(getLastCard()); }

    /**
     * Returns true when the slot contains no cards or if the card the player, wants to insert, has only one level more than the last one inserted
     * @param dCard The <code>DevelopmentCard</code> that the player wants to insert
     */
    public void addCard(DevelopmentCard dCard){
        if(canAddCard(dCard)){
            this.developmentCardList.add(dCard);
        }
            else throw new IllegalArgumentException("Unable to insert development card in this slot, change destination slot");
    }

    /**
     *
     * @return the total of points given by the sum of the <code>VictoryPoints</code> of each single card
     */
    public int countCardPoints(){

        return this.developmentCardList.stream()
                                            .map(Card::getVictoryPoints)
                                                .reduce(0, Integer::sum);
    }

    /**
     *
     * @return true if the slot contains no <code>DevelopmentCards</code>
     */
    public boolean isEmpty(){ return this.developmentCardList.size() == 0; }
}
