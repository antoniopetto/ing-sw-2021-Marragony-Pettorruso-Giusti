package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents the StrongBox.
 * It will contain all the <code>Resources</code> received as a result of the activated production powers.
 *
 * @see Resource
 */
public class StrongBox {

    private final Map<Resource, Integer> content;
    private VirtualView observer;

    /**
     * Constructs the StrongBox
     */
    public StrongBox() { this.content = new EnumMap<Resource, Integer>(Resource.class); }

    /**
     * @param r    The <code>Resource</code>
     * @return total units of <code>resource</code> r  inside the StrongBox
     */
    public int getQuantity(Resource r){ return content.getOrDefault(r,0); }

    /**
     * Adds a quantity of <code>resource</code> r inside the StrongBox
     *
     * @param r The <code>Resource</code> to add
     * @param quantity number of Resource r to add in StrongBox
     */
    public void addResource( Resource r, int quantity){

        if(quantity<=0) throw new IllegalArgumentException("No sense to enter a negative / equal to 0 quantity");

        if(content.containsKey(r))
            content.replace(r, content.get(r)+quantity);
            else content.put(r, quantity);
        observer.strongBoxUpdate();
    }

    /**
     * Removes a <code>resource</code> r unit from the StrongBox
     *
     * @param r The <code>Resource</code> to remove
     */
    public void removeResource( Resource r){
            if(getQuantity(r)>1)
                content.replace(r, content.get(r)-1);
                else if(getQuantity(r)==1) content.remove(r);
                    else if(getQuantity(r) == 0)
                        throw new IllegalArgumentException("In StrongBox there is no Resource r");
            observer.strongBoxUpdate();
    }

    /**
     *
     * @return the total amount of resources inside the StrongBox
     */
    public int countTotalResources(){
        return content.values()
                        .stream()
                            .reduce(0, Integer::sum);
    }

    public void setObserver(VirtualView observer)
    {
        this.observer=observer;
    }

    public Map<Resource, Integer> getContent() {
        return content;
    }
}
