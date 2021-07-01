package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleDepot;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This is a class with static methods used by many classes of the GUI package.
 */
public class GUISupport {

    private static String resourceString = null;

    /**
     * It returns the path of the png file of a resource
     */
    public static String returnPath(String resource){
        switch (resource) {
            case "COIN" -> {
                return "coin.png";
            }
            case "STONE" -> {
                return "stone.png";
            }
            case "SHIELD" -> {
                return "shield.png";
            }
            case "SERVANT" -> {
                return "servant.png";
            }
            default ->{return null;}
        }
    }

    /**
     * It returns the path of the png file of a marble.
     */
    public static String getMarblePath(String marble){
        switch (marble) {
            case "GREY" -> {
                return "black-marble.png";
            }
            case "BLUE" ->{
                return "blue-marble.png";
            }
            case "PURPLE" ->{
                return "purple-marble.png";
            }
            case "YELLOW" ->{
                return "yellow-marble.png";
            }
            case "WHITE" ->{
                return "white-marble.png";
            }
            case "RED" ->{
                return "red-marble.png";
            }
            default ->{
                return null;
            }
        }


    }

    /**
     * It handles the visibility of nodes.
     * @param visible tells if the nodes need to be visible
     * @param nodes are the nodes to handle.
     */
    public static void setVisible(boolean visible, Node... nodes){
        for(Node node:nodes){
            node.setVisible(visible);
        }
    }

    /**
     * It handles the disability of nodes.
     * @param disable tells if the nodes need to be disabled.
     * @param nodes are the nodes to handle.
     */
    public static void setDisable(boolean disable, Node... nodes){
        for (Node node:nodes)
            node.setDisable(disable);
    }

    /**
     * It sets the position of the faithMarker on the faith track in the main scene.
     * @param position is the position of the faithMarker.
     * @param faithMarker is the marker to set.
     */
    public static void setFaithTrack(int position, ImageView faithMarker)
    {
        if(position<3){
            faithMarker.setLayoutX(-55+(position*38));}
        else if(position<5){
            position=position-2;
            faithMarker.setLayoutY(107-(position*38));
            faithMarker.setLayoutX(19);
        }
        else if(position<10){
            faithMarker.setLayoutY(34);
            position = position-4;
            faithMarker.setLayoutX(19+(position*38));
        }
        else if (position<12){
            faithMarker.setLayoutX(203);
            position=position-9;
            faithMarker.setLayoutY(31+(position*38));
        }
        else if(position<17){
            faithMarker.setLayoutY(107);
            position=position-11;
            faithMarker.setLayoutX(203+(position*38));
        }
        else if(position<19){
            faithMarker.setLayoutX(389);
            position=position-16;
            faithMarker.setLayoutY(107-(position*38));
        }
        else{
            faithMarker.setLayoutY(34);
            position=position-18;
            faithMarker.setLayoutX(389+(position*38));
        }

    }

    /**
     * it is used to set visible a number equal to quantity of ImageView ( resources, leaderCard, DevCard)
     * @param quantity: number of image to set visible.
     * @param resources: a set of ImageView that may be set visible or invisible.
     */
    public static void settingImageView(int quantity, ImageView... resources){
        String path = "/images/res-marbles/";

        for(int i = 0; i < quantity; i++){
            resources[i].setImage(new Image(path + resourceString));
            resources[i].setVisible(true);
        }
        for(int i = quantity; i < resources.length; i++) resources[i].setVisible(false);
    }

    /**
     * it takes the 'resource' ImageView and sets it with 'resourceLeaderCard' Image.
     * @param resource: Imageview
     * @param resourceLeaderCard: url of resource to set.
     */
    public static void settingImageView(ImageView resource, String resourceLeaderCard){
        String path = "/images/res-marbles/";
        resourceString = returnPath(resourceLeaderCard);
        resource.setImage(new Image(path + resourceString));
        resource.setVisible(true);

    }

    /**
     * It sets the bloom effect on the element selected.
     * @param active tells if the effect is active
     * @param element is the element to set the effect
     */
    public static void setEffect(boolean active, Node element)
    {
        Bloom bloom=new Bloom();
        if(active)element.setEffect(bloom);
        else element.setEffect(null);
    }

    /**
     * It return the quantity of resources in a simple depot
     */
    public static int quantityOfResources(SimpleDepot simpleDepot){

        if(simpleDepot != null) {
           int  quantity = simpleDepot.getQuantity();
           if(simpleDepot.getResource() !=null) resourceString = returnPath(simpleDepot.getResource().toString());
            return quantity;
        } else return 0;

    }


}
