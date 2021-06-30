package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowController implements Initializable {

    @FXML
    private ImageView leaderCard1;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private Group wareHouse;
    @FXML
    private ImageView resHigh;
    @FXML
    private ImageView resMed1;
    @FXML
    private ImageView resMed2;
    @FXML
    private ImageView resLow1;
    @FXML
    private ImageView resLow2;
    @FXML
    private ImageView resLow3;
    @FXML
    private ImageView resExtra1s;
    @FXML
    private ImageView resExtra1d;
    @FXML
    private ImageView resExtra2s;
    @FXML
    private ImageView resExtra2d;
    @FXML
    private ImageView faithMarker;
    @FXML
    private Group slotsTot;
    @FXML
    private GridPane strongBox;
    @FXML
    private Text n1;
    @FXML
    private Text n2;
    @FXML
    private Text n3;
    @FXML
    private Text n4;
    @FXML
    private ImageView shield;
    @FXML
    private ImageView stone;
    @FXML
    private ImageView servant;
    @FXML
    private ImageView coin;

    private SimplePlayer thisPlayer;
    private boolean closeWindow = false;

    //TODO add resources in ExtraDepot and faith;

    public void setWareHouse(){
        GUISupport.setVisible(false, resHigh, resMed1, resMed2, resLow1, resLow2, resLow3);

        int quantity = GUISupport.quantityOfResources(thisPlayer.getWarehouse().getDepot(DepotName.HIGH));
        GUISupport.settingImageView(quantity, resHigh);

        quantity =  GUISupport.quantityOfResources(thisPlayer.getWarehouse().getDepot(DepotName.MEDIUM));
        GUISupport.settingImageView(quantity, resMed1, resMed2);

        quantity =  GUISupport.quantityOfResources(thisPlayer.getWarehouse().getDepot(DepotName.LOW));
        GUISupport.settingImageView(quantity, resLow1, resLow2, resLow3);

    }

    public void setLeaderCard(){
        int counter = 0;
        for(SimpleLeaderCard card : thisPlayer.getLeaderCards()){
            if(card.isActive()) {

                int quantity = 0;
                if(card.getAbility().getType().equals(SimpleAbility.Type.EXTRADEPOT)){
                    if(card.getAbility().getResource().equals(thisPlayer.getWarehouse().getDepot(DepotName.FIRST_EXTRA).getConstraint()))
                        quantity =GUISupport.quantityOfResources(thisPlayer.getWarehouse().getDepot(DepotName.FIRST_EXTRA));
                    else  quantity =GUISupport.quantityOfResources(thisPlayer.getWarehouse().getDepot(DepotName.SECOND_EXTRA));
                }

                if(counter == 0){
                    leaderCard1.setImage(new Image("/cards/leader/Leader-" + card.getId() + ".jpg"));
                    leaderCard1.setVisible(true);

                    if(quantity > 0) GUISupport.settingImageView(quantity, resExtra1s, resExtra1d);
                        else GUISupport.setVisible(false, resExtra1s, resExtra1d);

                } else {
                    leaderCard2.setImage(new Image("/cards/leader/Leader-" + card.getId() + ".jpg"));
                    leaderCard2.setVisible(true);

                    if(quantity > 0) GUISupport.settingImageView(quantity, resExtra2s, resExtra2d);
                         else GUISupport.setVisible(false, resExtra2s, resExtra2d);
                }
                counter++;
            }
        }
    }

    public void setGame(SimpleModel simpleModel, String username){
        for(SimplePlayer simplePlayer : simpleModel.getPlayers()){
            if(simplePlayer.getUsername().equals(username))
                this.thisPlayer = simplePlayer;
        }

    }

    public void setSlots(){
        int slotCounter = 0;
        int cardCounter;
        for(Node node : slotsTot.getChildren())
        {
            Group slot = (Group) node;
            cardCounter=0;
            for(Node cardNode : slot.getChildren())
            {
                boolean cardPresent = false;
                ImageView card = (ImageView)cardNode;
                if(thisPlayer.getSlots().get(slotCounter).getCards().size()>cardCounter){
                    SimpleDevCard simpleDevCard =thisPlayer.getSlots().get(slotCounter).getCards().get(cardCounter);
                    if(simpleDevCard!=null){
                        int id = simpleDevCard.getId();
                        String url = "/cards/development/Development-"+id+".jpg";
                        card.setImage(new Image(url));
                        card.setId(String.valueOf(id));
                        cardPresent=true;
                    }
                }
                GUISupport.setVisible(cardPresent, card);
                GUISupport.setDisable(!cardPresent, card);
                cardCounter++;
            }
            slotCounter++;
        }
    }

    public void setTrack(){
        int position= thisPlayer.getPosition();
        if(position<3){
            faithMarker.setLayoutX(15+(position*32));}
        else if(position<5){
            position=position-2;
            faithMarker.setLayoutY(161-(position*32));
            faithMarker.setLayoutX(79);
        }
        else if(position<10){
            faithMarker.setLayoutY(97);
            position = position-4;
            faithMarker.setLayoutX(79+(position*32));
        }
        else if (position<12){
            faithMarker.setLayoutX(240);
            position=position-9;
            faithMarker.setLayoutY(97+(position*32));
        }
        else if(position<17){
            faithMarker.setLayoutY(161);
            position=position-11;
            faithMarker.setLayoutX(240+(position*32));
        }
        else if(position<19){
            faithMarker.setLayoutX(400);
            position=position-16;
            faithMarker.setLayoutY(161-(position*32));
        }
        else{
            faithMarker.setLayoutY(97);
            position=position-18;
            faithMarker.setLayoutX(400+(position*32));
        }
    }

    public void setStrongBox(){
        int quantity;
        if(thisPlayer.getStrongbox().get(Resource.COIN)!=null)
            quantity = thisPlayer.getStrongbox().get(Resource.COIN);
        else quantity=0;
        if(quantity>0) {
            GUISupport.setVisible(true, coin);
            n4.setText(String.valueOf(quantity));
        }
        else GUISupport.setVisible(false, coin);



        if(thisPlayer.getStrongbox().get(Resource.SHIELD)!=null)
            quantity = thisPlayer.getStrongbox().get(Resource.SHIELD);
        else quantity=0;
        if(quantity>0) {
            GUISupport.setVisible(true, shield);
            n1.setText(String.valueOf(quantity));
        }
        else GUISupport.setVisible(false, shield);



        if(thisPlayer.getStrongbox().get(Resource.STONE)!=null)
            quantity = thisPlayer.getStrongbox().get(Resource.STONE);
        else quantity=0;
        if(quantity>0) {
            GUISupport.setVisible(true, stone);
            n2.setText(String.valueOf(quantity));
        }
        else GUISupport.setVisible(false, stone);


        if(thisPlayer.getStrongbox().get(Resource.SERVANT)!=null)
            quantity = thisPlayer.getStrongbox().get(Resource.SERVANT);
        else quantity=0;
        if(quantity>0) {
            GUISupport.setVisible(true, servant);
            n3.setText(String.valueOf(quantity));
        }
        else GUISupport.setVisible(false, servant);

    }

    @FXML
    public void closeShow(ActionEvent actionEvent) {
        actionEvent.consume();
        setCloseWindow(true);
    }

    public synchronized boolean isCloseWindow() {
        while(!closeWindow){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return closeWindow;
    }

    private synchronized void setCloseWindow(boolean close){
        closeWindow = close;
        notifyAll();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leaderCard2.setVisible(false);
        leaderCard1.setVisible(false);
    }
}
