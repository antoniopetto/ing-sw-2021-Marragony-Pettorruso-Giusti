package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleDevCard;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    private int choice = 0;
    private SimpleModel simpleModel = null;
    private int cardId = 0;


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
    private GridPane decks;


    @FXML
    private GridPane marketGrid;

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
        }
    }

    private void activeLeaderCardComponents(boolean active, int counter){

        if(counter > 0){
            activeCard1Radio.setVisible(active);
            activeCard1Radio.setDisable(!active);
            lCardButton.setDisable(!active);
            lCardButton.setVisible(active);
        }
        if(counter > 1){
            activeCard2Radio.setVisible(active);
            activeCard2Radio.setDisable(!active);
        }
    }

    @FXML
    public void selectLCard(ActionEvent actionEvent) {
        actionEvent.consume();

        if(leaderCardGroup.getSelectedToggle() == activeCard1Radio) setCardId(simpleModel.getThisPlayer().getLeaderCards().get(0).getId());
        else if(leaderCardGroup.getSelectedToggle() == activeCard2Radio) setCardId(simpleModel.getThisPlayer().getLeaderCards().get(1).getId());

        activeLeaderCardComponents(false, simpleModel.getThisPlayer().getLeaderCards().size());
        setActionButton(true);

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
                    leaderCard1.setVisible(true);
                    leaderCard1.setDisable(false);
                    leaderCard2.setDisable(false);
                    leaderCard2.setVisible(true);

                    if(active.get(0)){
                        activeCard1.setVisible(true);
                        activeCard1.setDisable(false);
                    }else{
                        activeCard1.setVisible(false);
                        activeCard1.setDisable(true);
                    }

                    if(active.get(1)){
                        activeCard2.setVisible(true);
                        activeCard2.setDisable(false);
                    }else{
                        activeCard2.setVisible(false);
                        activeCard2.setDisable(true);
                    }


                }
                case 1 -> {
                    leaderCard1.setImage(cards.get(0));
                    leaderCard1.setVisible(true);
                    leaderCard1.setDisable(false);
                    leaderCard2.setDisable(true);
                    leaderCard2.setVisible(false);

                    if(active.get(0)){
                        activeCard1.setVisible(true);
                        activeCard1.setDisable(false);
                    }else{
                        activeCard1.setVisible(false);
                        activeCard1.setDisable(true);
                    }
                }

            }
        } else {
            leaderCard1.setVisible(false);
            leaderCard1.setDisable(true);
            leaderCard2.setDisable(true);
            leaderCard2.setVisible(false);
            activeCard1.setVisible(false);
            activeCard1.setDisable(true);
            activeCard2.setVisible(false);
            activeCard2.setDisable(true);
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


    private void setActionButton( boolean active){
        pLCardButton.setVisible(active);
        pLCardButton.setDisable(!active);
        dLCardButton.setVisible(active);
        dLCardButton.setDisable(!active);
    }


    public void selectCol(ActionEvent actionEvent) {
    }

    public void setScene(SimpleModel simpleModel){
        this.simpleModel=simpleModel;
        setLeaderCard();
        setDecks();
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
            if(ids.get(i)!=null)
            {
                String url = "/cards/development/Development-" + ids.get(i) + ".jpg";
                imageview.setImage(new Image(url));
            }
            else imageview.setVisible(false);
            i++;
        }
    }

    
}
