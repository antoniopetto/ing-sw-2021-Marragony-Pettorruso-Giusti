package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.model.exceptions.ElementNotFoundException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The class represents an abstraction of a card, which has an id and the number of Victory Points.
 * Two real classes extend this class: DevelopmentCard and LeaderCard
 */

public abstract class Card implements Serializable {

    private final int victoryPoints;
    private final int id;

    /**
     * Card constructor.
     *
     * @param id                The card's id
     * @param victoryPoints     The card's victoryPoints
     */
    public Card(int id, int victoryPoints) {
        this.id = id;
        if(victoryPoints < 0)
            throw  new IllegalArgumentException();
        this.victoryPoints = victoryPoints;
    }

    /**
     * General parametric method that finds, in a <code>Collection</code> of <code>Identifiable</code> objects, the element with the desired <code>id</code>.
     *
     * @param id                            The <code>id</code> of the element to search.
     * @param collection                    The <Code>Collection</Code> where to search.
     * @param <E>                           The type of the object which is forming the <code>Collection</code>.
     * @return                              The first of the objects with the specified <code>id</code>.
     * @throws ElementNotFoundException     If no object in the <code>Collection</code> has the specified <code>id</code>.
     */
    public static <E extends Card> E getById(int id, Collection<E> collection) throws ElementNotFoundException {
        E result = collection.stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElse(null);
        if (result == null)
            throw new ElementNotFoundException("No object with id " + id);
        return result;
    }

    /**
     * Utility method to check if a list of cards contain duplicates ids.
     *
     * @param cards     The list of cards
     * @return          <code>true</code> if there are duplicates <code>false</code> otherwise
     */
    public static boolean containsDuplicates(List<? extends Card> cards){

        Set<Integer> uniqueIds = cards.stream().map(Card::getId).collect(Collectors.toSet());
        return (uniqueIds.size() != cards.size());
    }

    public int getVictoryPoints()
    {
        return this.victoryPoints;
    }

    public int getId() {
        return id;
    }
}
