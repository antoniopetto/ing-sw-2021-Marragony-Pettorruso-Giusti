package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;

public class GUISupport {

    private static String resourceString = null;

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
    public static void setVisible(boolean visible, Node... nodes){
        for(Node node:nodes){
            node.setVisible(visible);
        }
    }
    public static void setDisable(boolean disable, Node... nodes){
        for (Node node:nodes)
            node.setDisable(disable);
    }


    public static void settingImageView(int quantity, ImageView... resources){
        String path = "/res-marbles/";

        for(int i = 0; i < quantity; i++){
            resources[i].setImage(new Image(path + resourceString));
            resources[i].setVisible(true);
        }
    }

    public static int quantityOfResources(Map<Resource, Integer> map){

        if(map != null) {
            int quantity = 0;

            for (Resource resource1 : map.keySet()) {
                quantity = map.get(resource1);
                resourceString = returnPath(resource1.toString());
            }
            return quantity;
        }else return 0;

    }
}
