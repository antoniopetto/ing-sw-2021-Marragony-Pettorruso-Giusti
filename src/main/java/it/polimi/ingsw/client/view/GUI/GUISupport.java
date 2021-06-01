package it.polimi.ingsw.client.view.GUI;

import javafx.scene.Node;

public class GUISupport {

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
                return "servant";
            }
            default ->{return null;}
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
}
