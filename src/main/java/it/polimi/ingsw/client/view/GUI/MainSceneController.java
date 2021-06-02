package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleDevCard;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    private int choice = 0;
    private SimpleModel simpleModel = null;
    private int cardId = 0;
    private int bufferId = 0;


    //LeaderCard Action
    @FXML
    private Button pLCardButton;
    @FXML
    private Button dLCardButton;
    @FXML
    private RadioButton activeCard1Radio;
    @FXML
    private RadioButton activeCard2Radio;
    @FXML
    private Button lCardButton;
    @FXML
    private Label activeCard1;
    @FXML
    private Label activeCard2;
    @FXML
    private ImageView leaderCard1;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private ToggleGroup leaderCardGroup;
    //End

    @FXML
    private ImageView resourceHigh;
    @FXML
    private ImageView resourceSXMed;
    @FXML
    private ImageView resourceDXMed;
    @FXML
    private ImageView resourceCLow;
    @FXML
    private ImageView resourceSXLow;
    @FXML
    private ImageView resourceDXLow;
    @FXML
    private Button buyCardButton;
    @FXML
    private Button activateProductionButton;
    @FXML
    private Button endTurnButton;
    @FXML
    private Button buyResourcesButton;
    @FXML
    private GridPane decks;

    @FXML
    private Group slots;


    @FXML
    private GridPane marketGrid;
    @FXML
    private ImageView spareMarble;
    @FXML
    private Group resourceArrow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void clickActionButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Button button = (Button) actionEvent.getSource();

        switch (button.getId()){
            case "pLCardButton" -> {
                setChoice(1);

                if(simpleModel.getThisPlayer().getLeaderCards().size()== 0) setCardId(-1);
                else {
                    activeLeaderCardComponents(true, simpleModel.getThisPlayer().getLeaderCards().size());
                    setActionButton(false);
                }
            }
            case "dLCardButton" -> {
                setChoice(2);

                if(simpleModel.getThisPlayer().getLeaderCards().size()== 0) setCardId(-1);
                else {
                    activeLeaderCardComponents(true, simpleModel.getThisPlayer().getLeaderCards().size());
                    setActionButton(false);
                }
            }
            case "buyCardButton" -> {
                setChoice(4);
            }
            case "buyResourcesButton" ->{
                setChoice(3);
                resourceArrow.setVisible(true);
                setActionButton(false);
            }
            case "endTurnButton" -> setChoice(6);

        }
    }

    private void activeLeaderCardComponents(boolean active, int counter){
        if(counter > 0){
        GUISupport.setVisible(active, activeCard1Radio, lCardButton);
        }
        if(counter > 1){
            GUISupport.setVisible(active, activeCard2Radio);
        }
    }


    @FXML
    public void selectLCard(ActionEvent actionEvent) {
        actionEvent.consume();

        if(leaderCardGroup.getSelectedToggle() == activeCard1Radio) setCardId(simpleModel.getThisPlayer().getLeaderCards().get(0).getId());
        else if(leaderCardGroup.getSelectedToggle() == activeCard2Radio) setCardId(simpleModel.getThisPlayer().getLeaderCards().get(1).getId());

        activeLeaderCardComponents(false, simpleModel.getThisPlayer().getLeaderCards().size());

    }

    public void setLeaderCard(){

        if(simpleModel.getThisPlayer().getLeaderCards().size() > 0){
            List<Image> cards = new ArrayList<>();
            List<Boolean> active = new ArrayList<>();
            for (SimpleLeaderCard card : simpleModel.getThisPlayer().getLeaderCards()) {
                int cardId = card.getId();
                String imUrl = "/cards/leader/Leader-" + cardId + ".jpg";
                cards.add(new Image(imUrl));
                if(card.isActive()) active.add(true);
                    else active.add(false);
            }

            switch (cards.size()) {
                case 2 -> {
                    leaderCard1.setImage(cards.get(0));
                    leaderCard2.setImage(cards.get(1));
                    GUISupport.setVisible(true, leaderCard1, leaderCard2);

                    if(active.get(0)){
                        GUISupport.setVisible(true, activeCard1);

                    }else{
                        GUISupport.setVisible(false, activeCard1);

                    }

                    if(active.get(1)){
                        GUISupport.setVisible(true, activeCard2);

                    }else{
                        GUISupport.setVisible(false, activeCard2);

                    }


                }
                case 1 -> {
                    leaderCard1.setImage(cards.get(0));
                    leaderCard1.setVisible(true);
                    leaderCard2.setVisible(false);

                    if(active.get(0)){
                        activeCard1.setVisible(true);
                    }else{
                        activeCard1.setVisible(false);
                    }
                }

            }
        } else {
            GUISupport.setVisible(false, leaderCard1, leaderCard2, activeCard1, activeCard2);

        }

    }


    public synchronized int getChoice() {
        while (choice == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int tmpChoice = choice;
        choice = 0;
        return tmpChoice;
    }

    public synchronized void setChoice(int choice) {
        this.choice = choice;
        notifyAll();
    }

    public synchronized int getCardId() {
        while (cardId == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int tmpCardId = cardId;
        cardId = 0;
        return tmpCardId;
    }

    public synchronized void setCardId(int cardId) {
        this.cardId = cardId;
        notifyAll();
    }


    public void setActionButton( boolean postTurn){
        if(!postTurn){
            GUISupport.setVisible(true, pLCardButton, dLCardButton, buyResourcesButton, buyCardButton, activateProductionButton);
            endTurnButton.setVisible(postTurn);
        }else{
            GUISupport.setVisible(false, buyResourcesButton, buyCardButton, activateProductionButton);
            endTurnButton.setVisible(true);
            GUISupport.setDisable(false, pLCardButton, dLCardButton);
        }
    }


    public void setScene(SimpleModel simpleModel){
        this.simpleModel=simpleModel;
        setActionButton(false);
        setResourcesInDepot();
        setLeaderCard();
        setDecks();
        setMarketBoard();
        disableCards(true);
        disableSlots(true);
    }

    public void disableCards(boolean disable) {GUISupport.setDisable(disable, decks);}
    public void disableSlots(boolean disable) {GUISupport.setVisible(!disable, slots);}



    public void setDecks()
    {
        List<Integer> ids = new ArrayList<>();
        for (SimpleDevCard card: simpleModel.getDevCardDecks()) {
            ids.add(card.getId());
        }

        int i =0;

        for (Node node: decks.getChildren()) {
            ImageView imageview = (ImageView) node;
            if(ids.get(i)!=null)
            {
                String url = "/cards/development/Development-" + ids.get(i) + ".jpg";
                imageview.setImage(new Image(url));
                imageview.setId(String.valueOf(ids.get(i)));
            }
            else imageview.setVisible(false);
            i++;
        }
    }

    public void setMarketBoard(){

        List<String> marbleColor = new ArrayList<>();

        for(int i = 0; i < simpleModel.getMarketBoard().length; i++){
            for(int j = 0; j < simpleModel.getMarketBoard()[i].length; j++){
                marbleColor.add(simpleModel.getMarketBoard()[i][j].toString());
            }
        }


        int i = 0;
        for (Node node: marketGrid.getChildren()) {
            ImageView imageview = (ImageView) node;
            if(marbleColor.get(i)!=null)
            {
                String url = "/res-marbles/" + GUISupport.getMarblePath(marbleColor.get(i));
                imageview.setImage(new Image(url));
            }
            else imageview.setVisible(false);
            i++;
        }

        spareMarble.setImage(new Image("/res-marbles/" + GUISupport.getMarblePath(simpleModel.getSpareMarble().toString())));
    }

    @FXML
    public void selectMarbleBuffer(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ImageView arrow = (ImageView) mouseEvent.getSource();
        switch (arrow.getId()){
           case "firstCol" -> setBufferId(1);
           case "secondCol" -> setBufferId(2);
           case "thirdCol" -> setBufferId(3);
           case "fourthCol" -> setBufferId(4);
           case "firstRow" -> setBufferId(5);
           case "secondRow" -> setBufferId(6);
           case "thirdRow" -> setBufferId(7);
        }
    }

    private synchronized void setBufferId(int bufferId){
        this.bufferId = bufferId;
        resourceArrow.setVisible(false);
        notifyAll();
    }

    public synchronized int getBufferId(){
        while (bufferId == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int tmpId = bufferId;
        bufferId = 0;
        return tmpId;
    }

    public void setResourcesInDepot(){
        int quantity;

        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.HIGH));
        GUISupport.settingImageView(quantity, resourceHigh);

        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.MEDIUM));
        GUISupport.settingImageView(quantity, resourceSXMed, resourceDXMed);


        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.LOW));
        GUISupport.settingImageView(quantity, resourceSXLow, resourceCLow, resourceDXLow);
    }


    public void devCardSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(()->{
            ImageView image = (ImageView)mouseEvent.getSource();
            setCardId(Integer.parseInt(image.getId()));
        });
    }


    public void slotSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(()->{
            Rectangle slot = (Rectangle)mouseEvent.getSource();
            String slotId = slot.getId();
            switch (slotId){
                case "slot1" -> setChoice(1);
                case "slot2" -> setChoice(2);
                case "slot3" -> setChoice(3);
            }
        });
    }
}
