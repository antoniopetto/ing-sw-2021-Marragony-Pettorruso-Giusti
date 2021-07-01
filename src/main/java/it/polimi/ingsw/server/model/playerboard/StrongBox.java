package it.polimi.ingsw.server.model.playerboard;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.shared.Resource;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents the StrongBox.
 * It will contain all the <code>Resources</code> received as a result of the activated production powers.
 *
 * @see Resource
 */
public class StrongBox implements Serializable {

    private final Map<Resource, Integer> content;
    private transient VirtualView virtualView;

    /**
     * Constructs the StrongBox
     */
    public StrongBox() {
        this.content = new EnumMap<Resource, Integer>(Resource.class);
    }

    public void setVirtualView(VirtualView virtualView){
        this.virtualView = virtualView;
    }

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
    public void addResource(Resource r, int quantity){

        if (quantity <= 0)
            throw new IllegalArgumentException("Cannot add non positive quantity");

        if (content.containsKey(r))
            content.put(r, content.get(r) + quantity);
        else
            content.put(r, quantity);

        virtualView.strongBoxUpdate();
    }

    /**
     * Removes a <code>resource</code> r unit from the StrongBox
     *
     * @param r The <code>Resource</code> to remove
     */
    public void removeResource(Resource r){
            if(getQuantity(r) > 1)
                content.replace(r, content.get(r)-1);
            else if(getQuantity(r) == 1)
                content.remove(r);
            else if(getQuantity(r) == 0)
                throw new IllegalArgumentException("In StrongBox there are no " + r + " left");

            virtualView.strongBoxUpdate();
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

    public Map<Resource, Integer> getContent() {
        return content;
    }
}
