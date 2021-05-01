package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card {

    private final int victoryPoints;
    private final int id;

    /**
     * General parametric method that finds, in a <code>Collection</code> of <code>Identifiable</code> objects, the element with the desired <code>id</code>.
     *
     * @param id                            The <code>id</code> of the element to search.
     * @param collection                    The <Code>Collection</Code> where to search.
     * @param <E>                           The type of the object which is forming the <code>Collection</code>.
     * @return                              The first of the objects with the specified <code>id</code>.
     * @throws ElementNotFoundException     If no object in the <code>Collection</code> has the specified <code>id</code>.
     */
    public static <E extends Card> E getById(int id, Collection<E> collection){
        E result = collection.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(null);
        if (result == null)
            throw new ElementNotFoundException("No object has the specified id");
        return result;
    }

    public static boolean containsDuplicates(List<? extends Card> cards){

        Set<Integer> uniqueIds = cards.stream().map(Card::getId).collect(Collectors.toSet());
        return (uniqueIds.size() != cards.size());
    }

    public Card(int id, int victoryPoints) {
        this.id = id;
        if(victoryPoints < 0)
            throw  new IllegalArgumentException();
        this.victoryPoints = victoryPoints;
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

    public int getId() {
        return id;
    }
}
