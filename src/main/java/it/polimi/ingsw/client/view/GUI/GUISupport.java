package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleDepot;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
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

    public static void setFaithTrack(SimplePlayer player, ImageView faithMarker)
    {
        int position = player.getPosition();
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

    public static void settingImageView(int quantity, ImageView... resources){
        String path = "/res-marbles/";

        for(int i = 0; i < quantity; i++){
            resources[i].setImage(new Image(path + resourceString));
            resources[i].setVisible(true);
        }
    }

    public static void setEffect(boolean active, Node element)
    {
        Bloom bloom=new Bloom();
        if(active)element.setEffect(bloom);
        else element.setEffect(null);
    }

    public static int quantityOfResources(SimpleDepot simpleDepots){

        if(simpleDepots != null) {
           int  quantity = simpleDepots.getQuantity();
           if(simpleDepots.getResource() !=null) resourceString = returnPath(simpleDepots.getResource().toString());
            return quantity;
        }else return 0;

    }
}
