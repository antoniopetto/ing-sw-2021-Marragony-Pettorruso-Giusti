package it.polimi.ingsw.model.playerboard;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import java.util.ArrayList;
import java.util.List;


public class Slot {
    private List<DevelopmentCard> developmentCardList;

    public Slot(){
        this.developmentCardList = new ArrayList<>();
    }

    public DevelopmentCard getCard(){
        return this.developmentCardList.get(developmentCardList.size()-1);
    }

    public boolean addCard(DevelopmentCard dCard){
        if(isEmpty() || dCard.getLevel()==(getCard().getLevel()+1) )
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
