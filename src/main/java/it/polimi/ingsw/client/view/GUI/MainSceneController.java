package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.PopeFavourTile;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    private int choice = 0;
    private SimpleModel simpleModel = null;
    private int cardId = 0;
    private int bufferId = 0;
    private List<Node> elementsWithEffects = new ArrayList<>();
    private int numberOfVaticanReport =0;


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
    private ImageView leaderCard1;
    @FXML
    private ImageView leaderCard2;
    @FXML
    private ToggleGroup leaderCardGroup;
    @FXML
    private Rectangle rectCard1;
    @FXML
    private Rectangle rectCard2;
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
    private ImageView resourceSXExtra1;
    @FXML
    private ImageView resourceDXExtra1;
    @FXML
    private ImageView resourceSXExtra2;
    @FXML
    private ImageView resourceDXExtra2;
    @FXML
    private Button buyCardButton;
    @FXML
    private Button activateProductionButton;
    @FXML
    private Button endTurnButton;
    @FXML
    private Button buyResourcesButton;
    @FXML
    private Button confirmSelectionButton;
    @FXML
    private GridPane decks;
    @FXML
    private TitledPane showOthers;
    @FXML
    private Button show1;
    @FXML
    private Button show2;
    @FXML
    private Button show3;
    @FXML
    private Group slots;
    @FXML
    private Group strbxServant;
    @FXML
    private Group strbxCoin;
    @FXML
    private Group strbxShield;
    @FXML
    private Group strbxStone;
    @FXML
    private Group leaderResources;
    @FXML
    private Accordion actionButtons;
    @FXML
    private ImageView tile1;
    @FXML
    private ImageView tile2;
    @FXML
    private ImageView tile3;
    @FXML
    private Group cardsSlots;
    @FXML
    private Rectangle basePower;
    @FXML
    private GridPane marketGrid;
    @FXML
    private ImageView spareMarble;
    @FXML
    private Group resourceArrow;
    @FXML
    private ImageView faithMarker;
    @FXML
    private TextFlow logText;
    @FXML
    private ScrollPane log;
    @FXML
    private ImageView blackCross;

    private String username;
    private List<String> allUsername = new ArrayList<>();


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
            case "activateProductionButton" ->{
                showBasePower(true);
                setChoice(5);
                setActionButton(false);
            }
            case "endTurnButton" ->{
                setChoice(6);
                setActionButton(false);
            }
            case "show1" ->{
                if(allUsername.size() > 0) username = allUsername.get(0);
                setChoice(7);
                setActionButton(false);
            }
            case "show2" ->{
                if(allUsername.size() > 1) username = allUsername.get(1);
                setChoice(7);
                setActionButton(false);
            }
            case "show3" ->{
                if(allUsername.size() > 2) username = allUsername.get(2);
                setChoice(7);
                setActionButton(false);
            }

        }
    }

    public String getUser(){
        return username;
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
            List<SimpleLeaderCard> simpleLeaderCards = new ArrayList<>();

            for (SimpleLeaderCard card : simpleModel.getThisPlayer().getLeaderCards()) {
                int cardId = card.getId();
                String imUrl = "/cards/leader/Leader-" + cardId + ".jpg";
                cards.add(new Image(imUrl));
                simpleLeaderCards.add(card);
            }

            switch (cards.size()) {
                case 2 -> {
                    leaderCard1.setImage(cards.get(0));
                    leaderCard2.setImage(cards.get(1));
                    GUISupport.setVisible(true, leaderCard1, leaderCard2);

                    if(simpleLeaderCards.get(0).isActive()){
                        rectCard1.setVisible(true);
                        if(simpleLeaderCards.get(0).getAbility().getType().equals(SimpleAbility.Type.EXTRADEPOT)){
                            int quantity = 0;
                            if(simpleLeaderCards.get(0).getAbility().getResource().equals(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA).getConstraint()))
                              quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA));
                                else  quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.SECOND_EXTRA));

                            GUISupport.settingImageView(quantity, resourceSXExtra1, resourceDXExtra1);
                        }
                        else GUISupport.setVisible(false, resourceSXExtra1, resourceDXExtra1);
                    }else{
                        rectCard1.setVisible(false);
                    }


                    if(simpleLeaderCards.get(1).isActive()){
                        rectCard2.setVisible(true);
                        if(simpleLeaderCards.get(1).getAbility().getType().equals(SimpleAbility.Type.EXTRADEPOT)){
                            int quantity;
                            if(simpleLeaderCards.get(1).getAbility().getResource().equals(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA).getConstraint()))
                                quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA));
                            else  quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.SECOND_EXTRA));

                            GUISupport.settingImageView(quantity, resourceSXExtra2, resourceDXExtra2);
                        }
                        else GUISupport.setVisible(false, resourceSXExtra2, resourceDXExtra2);
                    }else{
                        rectCard2.setVisible(false);
                    }
                }
                case 1 -> {
                    leaderCard1.setImage(cards.get(0));
                    leaderCard1.setVisible(true);
                    leaderCard2.setVisible(false);

                    if(simpleLeaderCards.get(0).isActive()){
                        rectCard1.setVisible(true);
                        if(simpleLeaderCards.get(0).getAbility().getType().equals(SimpleAbility.Type.EXTRADEPOT)){
                            int quantity;
                            if(simpleLeaderCards.get(0).getAbility().getResource().equals(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA).getConstraint()))
                                quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA));
                            else  quantity =GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.SECOND_EXTRA));

                            GUISupport.settingImageView(quantity, resourceSXExtra1, resourceDXExtra1);
                        }
                        else GUISupport.setVisible(false, resourceSXExtra1, resourceDXExtra1);
                    }else{
                        rectCard1.setVisible(false);
                    }
                }

            }
        } else {
            GUISupport.setVisible(false, leaderCard1, leaderCard2, rectCard1, rectCard2);

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
        setWarehouse();
        setLeaderCard();
        setDecks();
        setMarketBoard();
        setSlots();
        setStrongbox();
        disableCards(true);
        disableSlots(true);
        disableButtons(false);
        disableCardsInSlot(true);
    }

    public void disableCards(boolean disable) {GUISupport.setDisable(disable, decks);}
    public void disableSlots(boolean disable) {GUISupport.setVisible(!disable, slots);}
    public void disableButtons(boolean disable) {GUISupport.setDisable(disable, actionButtons);}
    public void disableCardsInSlot(boolean disable) {GUISupport.setDisable(disable, cardsSlots);}
    public void showBasePower(boolean show){GUISupport.setVisible(show, basePower);}
    public void disableLeaderCards(boolean disable){
        GUISupport.setDisable(disable, leaderCard1);
        GUISupport.setDisable(disable, leaderCard2);
    }
    public void showLeaderResources(boolean show){GUISupport.setVisible(show, leaderResources);}
    public void showConfirmButton(boolean show){
        GUISupport.setVisible(!show, buyCardButton, buyResourcesButton, activateProductionButton);
        GUISupport.setVisible(show, confirmSelectionButton);
    }
    public void removeEffects(){
        for (Node node:elementsWithEffects) {
            GUISupport.setEffect(false, node);
        }
    }

    public void setFaithTrack(){GUISupport.setFaithTrack(simpleModel.getThisPlayer().getPosition(), faithMarker);}
    public void setRival(){GUISupport.setFaithTrack(simpleModel.getRivalPosition(), blackCross);}
    public void setSinglePlayerGame(){
        GUISupport.setVisible(true, blackCross);
        GUISupport.setVisible(false, showOthers);

    }
    public void setUsernameShow(){
        if(allUsername.size() > 0) allUsername.clear();
        for(SimplePlayer simplePlayer : simpleModel.getPlayers()){
            if(!simplePlayer.getUsername().equals(simpleModel.getThisPlayer().getUsername()))
                allUsername.add(simplePlayer.getUsername());
        }
        showUsername(allUsername, show1, show2, show3);
    }

    public void showUsername(List<String> username, Button... showButtons){
        for(int i = 0; i < username.size(); i++){
            showButtons[i].setVisible(true);
            showButtons[i].setText("Show " + username.get(i));
        }
        for(int i = username.size(); i < showButtons.length; i++){
            showButtons[i].setVisible(false);
        }
    }

    public void setDecks()
    {
        List<Integer> ids = new ArrayList<>();
        for (SimpleDevCard card: simpleModel.getDevCardDecks()) {
            ids.add(card.getId());
        }

        int i =0;

        for (Node node: decks.getChildren()) {
            ImageView imageview = (ImageView) node;
            if(i<ids.size()&&ids.get(i)!=null)
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

    public void setWarehouse(){
        GUISupport.setVisible(false, resourceHigh, resourceSXMed, resourceDXMed, resourceSXLow, resourceCLow, resourceDXLow);

        int quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.HIGH));
        GUISupport.settingImageView(quantity, resourceHigh);

        quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.MEDIUM));
        GUISupport.settingImageView(quantity, resourceSXMed, resourceDXMed);

        quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.LOW));
        GUISupport.settingImageView(quantity, resourceSXLow, resourceCLow, resourceDXLow);
    }

    public void setTiles(){
        List<PopeFavourTile> tiles=simpleModel.getThisPlayer().getTiles();
        if(tiles.get(0).isGained()) tile1.setImage(new Image("/pope-favor/pope_favor1_front.png"));
        else if(getMaxPosition()>8) GUISupport.setVisible(false, tile1);
        if(tiles.get(1).isGained()) tile2.setImage(new Image("/pope-favor/pope_favor2_front.png"));
        else if(getMaxPosition()>16) GUISupport.setVisible(false, tile2);
        if(tiles.get(2).isGained()) tile3.setImage(new Image("/pope-favor/pope_favor3_front.png"));
    }

    private int getMaxPosition(){
        int max=0;
        for (SimplePlayer player: simpleModel.getPlayers()) {
            if(player.getPosition()>max) max=player.getPosition();
        }
        return max;
    }

    public void setSlots() {
        int slotCounter = 0;
        int cardCounter;
        for(Node node : cardsSlots.getChildren())
        {
            Group slot = (Group) node;
            cardCounter=0;
            for(Node cardNode : slot.getChildren())
            {
                boolean cardPresent = false;
                ImageView card = (ImageView)cardNode;
                if(simpleModel.getThisPlayer().getSlots().get(slotCounter).getCards().size()>cardCounter){
                    SimpleDevCard simpleDevCard =simpleModel.getThisPlayer().getSlots().get(slotCounter).getCards().get(cardCounter);
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


    public void setStrongbox()
    {
        int count=0;
        for (Node node:strbxCoin.getChildren()) {
            if(count==1){
                Text text = (Text)node;
                int quantity;
                if(simpleModel.getThisPlayer().getStrongbox().get(Resource.COIN)!=null)
                    quantity = simpleModel.getThisPlayer().getStrongbox().get(Resource.COIN);
                else quantity=0;
                if(quantity>0) {
                    GUISupport.setVisible(true, strbxCoin);
                    text.setText(String.valueOf(quantity));
                }
                else GUISupport.setVisible(false, strbxCoin);
            }
            count++;
        }
        count=0;
        for (Node node:strbxShield.getChildren()) {
            if(count==1){
                Text text = (Text)node;
                int quantity;
                if(simpleModel.getThisPlayer().getStrongbox().get(Resource.SHIELD)!=null)
                    quantity = simpleModel.getThisPlayer().getStrongbox().get(Resource.SHIELD);
                else quantity=0;
                if(quantity>0) {
                    GUISupport.setVisible(true, strbxShield);
                    text.setText(String.valueOf(quantity));
                }
                else GUISupport.setVisible(false, strbxShield);
            }
            count++;
        }
        count=0;
        for (Node node:strbxStone.getChildren()) {
            if(count==1){
                Text text = (Text)node;
                int quantity;
                if(simpleModel.getThisPlayer().getStrongbox().get(Resource.STONE)!=null)
                    quantity = simpleModel.getThisPlayer().getStrongbox().get(Resource.STONE);
                else quantity=0;
                if(quantity>0) {
                    GUISupport.setVisible(true, strbxStone);
                    text.setText(String.valueOf(quantity));
                }
                else GUISupport.setVisible(false, strbxStone);
            }
            count++;
        }
        count=0;
        for (Node node:strbxServant.getChildren()) {
            if(count==1){
                Text text = (Text)node;
                int quantity;
                if(simpleModel.getThisPlayer().getStrongbox().get(Resource.SERVANT)!=null)
                    quantity = simpleModel.getThisPlayer().getStrongbox().get(Resource.SERVANT);
                else quantity=0;
                if(quantity>0) {
                    GUISupport.setVisible(true, strbxServant);
                    text.setText(String.valueOf(quantity));
                }
                else GUISupport.setVisible(false, strbxServant);
            }
            count++;
        }
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

    public void addTextInLog(String text)
    {
        if(text!=null){
            Text newText = new Text(text+"\n");
            newText.setFill(Color.BLACK);
            newText.setFont(Font.font("Lucida Calligraphy", 15));
            /*
            String previous = logText.getText();
            logText.setText(previous+"\n"+text);
             */
            Platform.runLater(()->logText.getChildren().add(newText));
            log.setVvalue(1.0);

        }

    }

    public void prodCardSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(()->{
            setChoice(2);
            ImageView image = (ImageView)mouseEvent.getSource();
            GUISupport.setEffect(true, image);
            elementsWithEffects.add(image);
            setCardId(Integer.parseInt(image.getId()));
        });
    }

    public void confirmProduction(ActionEvent actionEvent) {
        actionEvent.consume();
        Platform.runLater(()->setChoice(1));
    }

    public void basePowerSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(()->setChoice(3));
    }

    public void leaderProductionPower(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ImageView card = (ImageView)mouseEvent.getSource();
        String url = card.getImage().getUrl();
        int cardId=0;
        if(url.contains("113")) cardId=113;
        else if(url.contains("114")) cardId=114;
        else if (url.contains("115")) cardId=115;
        else if(url.contains("116")) cardId=116;
        if(cardId!=0) {
            GUISupport.setEffect(true, card);
            elementsWithEffects.add(card);
            int finalCardId = cardId;
            Platform.runLater(()->{
                setChoice(4);
                setCardId(finalCardId);
            });
        }

    }

    public void leaderResourceSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ImageView resource = (ImageView)mouseEvent.getSource();
        int choice =0;
        switch (resource.getId()){
            case "shield" ->choice=1;
            case "stone" ->choice=2;
            case "servant" ->choice=3;
            case "coin" ->choice=4;
        }
        int finalChoice = choice;
        Platform.runLater(()->setChoice(finalChoice));

    }
}
