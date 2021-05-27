package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class setUpController implements Initializable {


    @FXML
    private ImageView imCard1;
    @FXML
    private ImageView imCard2;
    @FXML
    private ImageView imCard3;
    @FXML
    private ImageView imCard4;

    private int result=0;
    private List<Integer> cardIds = new ArrayList<>();
    private SimpleModel game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void selectedCard(MouseEvent mouseEvent) {
            mouseEvent.consume();
            Platform.runLater(()->{
                ImageView card = (ImageView)mouseEvent.getSource();

                switch (card.getId()) {
                    case "imCard1" -> setResult(cardIds.get(2));
                    case "imCard2" -> setResult(cardIds.get(0));
                    case "imCard3" -> setResult(cardIds.get(1));
                    case "imCard4" -> setResult(cardIds.get(3));
                }
            });


    }



    private synchronized void setResult(int result){
        this.result = result;
        notifyAll();
    }


    public void setGame(SimpleModel game) {
        this.game = game;
        List<Image> cards = new ArrayList<>();
        

        for (SimpleLeaderCard card : game.getThisPlayer().getLeaderCards()) {
            int cardId = card.getId();
            cardIds.add(cardId);
            String imUrl = "/cards/leader/Leader-" + cardId + ".jpg";
            cards.add(new Image(imUrl));
        }
        switch (cards.size()) {
            case 4 -> {
                imCard1.setImage(cards.get(2));
                imCard2.setImage(cards.get(0));
                imCard3.setImage(cards.get(1));
                imCard4.setImage(cards.get(3));
            }
            case 3 -> {
                imCard1.setImage(cards.get(2));
                imCard2.setImage(cards.get(0));
                imCard3.setImage(cards.get(1));
                imCard4.setVisible(false);
                imCard4.setDisable(true);
            }
            case 2 -> {
                imCard2.setImage(cards.get(0));
                imCard3.setImage(cards.get(1));
                imCard4.setVisible(false);
                imCard4.setDisable(true);
                imCard3.setVisible(false);
                imCard3.setDisable(true);
            }
            case 1 -> {
                    imCard2.setImage(cards.get(0));
                    imCard3.setDisable(true);
                    imCard3.setVisible(false);
                    imCard4.setVisible(false);
                    imCard4.setDisable(true);
                    imCard1.setVisible(false);
                    imCard1.setDisable(true);
            }
            case 0 -> setResult(0);
        }

    }

    public synchronized int getResult() {
        while (result == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
