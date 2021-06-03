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
    private Button card1;
    @FXML
    private Button card2;
    @FXML
    private Button card3;
    @FXML
    private Button card4;
    @FXML
    private ImageView imCard1;
    @FXML
    private ImageView imCard2;
    @FXML
    private ImageView imCard3;
    @FXML
    private ImageView imCard4;

    private int result=0;

    private boolean isFirst = true;

    private List<Integer> cardIds = new ArrayList<>();
    private SimpleModel game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void selectedCard(MouseEvent mouseEvent) {
            mouseEvent.consume();
            Platform.runLater(()->{
                Button card = (Button)mouseEvent.getSource();
                switch (card.getId()) {
                    case "card1" -> result = cardIds.get(0);
                    case "card2" -> result = cardIds.get(1);
                    case "card3" -> result = cardIds.get(2);
                    case "card4" -> result = cardIds.get(3);
                }
                System.out.println();
            });


    }

    @FXML
    public void confirm(MouseEvent mouseEvent) {
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
                imCard1.setImage(cards.get(0));
                imCard2.setImage(cards.get(1));
                imCard3.setImage(cards.get(2));
                imCard4.setImage(cards.get(3));
            }
            case 3 -> {
                imCard1.setImage(cards.get(0));
                imCard2.setImage(cards.get(1));
                imCard3.setImage(cards.get(2));
                imCard4.setVisible(false);
                card4.setVisible(false);
            }
        }
        isFirst=true;
    }

    public synchronized int getResult() {
        return result;
    }
}
