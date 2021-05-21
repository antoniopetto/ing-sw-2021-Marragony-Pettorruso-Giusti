package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private SimpleGame game;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Image> cards = new ArrayList<>();
        for(SimpleLeaderCard card : game.getThisPlayer().getLeaderCards()){
            int cardId= card.getId();
            String imUrl = "/cards/leader/Leader-"+cardId+".jpg";
            cards.add(new Image(imUrl));
        }
        switch (cards.size())
        {
            case 4 ->
                {
                    imCard1.setImage(cards.get(0));
                    imCard2.setImage(cards.get(1));
                    imCard3.setImage(cards.get(2));
                    imCard4.setImage(cards.get(3));
                }
            case 3->{
                imCard1.setImage(cards.get(0));
                imCard2.setImage(cards.get(1));
                imCard3.setImage(cards.get(2));
                imCard3.setVisible(false);
                card3.setVisible(false);
            }

        }

    }

    public int selectedCard(MouseEvent mouseEvent) {
        return 0;
    }

    public void confirm(MouseEvent mouseEvent) {
    }

    public void setGame(SimpleGame game) {
        this.game = game;
    }
}
