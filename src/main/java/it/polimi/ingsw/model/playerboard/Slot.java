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
    private int id;

    public Slot(int id){
        this.developmentCardList = new ArrayList<>();
        this.id = id;
    }

    public int getId() { return id; }

    public DevelopmentCard getLastCard(){
        return this.developmentCardList.get(developmentCardList.size()-1);
    }

    public List<DevelopmentCard> getDevelopmentCardList() { return developmentCardList; }

    public boolean addCard(DevelopmentCard dCard){
        if(isEmpty() || dCard.getLevel()==(getLastCard().getLevel()+1) )
        {
            this.developmentCardList.add(dCard);
            return true;
        }
        return false;
    }
    public int countCardPoints(){
        int totalCardPoints = 0;
        for(DevelopmentCard developmentCard : this.developmentCardList)
            totalCardPoints += developmentCard.getVictoryPoints();

        return totalCardPoints;
    }

    public boolean isEmpty(){
        if(this.developmentCardList.size()==0) return true;
            else return false;
    }
}
