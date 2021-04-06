package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Slot.
 * It will contain a maximum of three <code>DevelopmentCards</code>
 *
 * @see DevelopmentCard
 */
public class Slot {

    private List<DevelopmentCard> developmentCardList;
    private final int id;

    /**
     *Construct a slot
     * @param id  It identifies this slot
     */
    public Slot(int id){
        this.developmentCardList = new ArrayList<>();
        this.id = id;
    }

    public int getId() { return id; }

    /**
     *
     * @return the last <code>DevelopmentCard</code> inserted in this slot
     */
    public DevelopmentCard getLastCard(){
        return this.developmentCardList.get(developmentCardList.size()-1);
    }

    /**
     *
     * @return all the <code>DevelopmentCard</code> in this slot
     */
    public List<DevelopmentCard> getDevelopmentCardList() { return developmentCardList; }

    /**
     * Returns true when the slot contains no cards or if the card the player, wants to insert, has only one level more than the last one inserted
     * @param dCard The <code>DevelopmentCard</code> that the player wants to insert
     * @return true if is possible to insert dCard in this slot alternatively false
     */
    public boolean addCard(DevelopmentCard dCard){
        if(isEmpty() || dCard.isLevelHigher(getLastCard()) )
        {
            this.developmentCardList.add(dCard);
            return true;
        }
        return false;
    }

    /**
     *
     * @return the total of points given by the sum of the <code>VictoryPoints</code> of each single card
     */
    public int countCardPoints(){
        int totalCardPoints = 0;
        for(DevelopmentCard developmentCard : this.developmentCardList)
            totalCardPoints += developmentCard.getVictoryPoints();

        return totalCardPoints;
    }

    /**
     *
     * @return true if the slot contains no <code>DevelopmentCards</code>
     */
    public boolean isEmpty(){
        if(this.developmentCardList.size()==0) return true;
            else return false;
    }
}
