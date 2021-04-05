package it.polimi.ingsw.model.playerboard;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the StrongBox.
 * It will contain all the <code>Resources</code> received as a result of the activated production powers.
 *
 * @see Resource
 */
public class StrongBox {
    protected Map<Resource, Integer> content;

    /**
     * Constructs the StrongBox
     */
    public StrongBox() { this.content = new HashMap<>(); }

    /**
     * @param r    The <code>Resource</code>
     * @return total units of <code>resource</code> r  inside the StrongBox
     */
    public int getQuantity(Resource r){ return content.get(r); }

    /**
     * Adds a <code>resource</code> r unit inside the StrongBox
     * @param r The <code>Resource</code> to add
     */
    public void addResource( Resource r){
        Integer integer = 1;
        if(content.containsKey(r))
            content.replace(r, content.get(r)+1);
            else content.put(r, integer);
    }

    /**
     * Removes a <code>resource</code> r unit from the StrongBox
     *
     * @param r The <code>Resource</code> to remove
     */
    public boolean removeResource( Resource r){
            if(getQuantity(r)>1)
                content.replace(r, content.get(r)-1);
                else if(getQuantity(r)==1) content.remove(r);
                    else return false;
                    return true;
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

}
