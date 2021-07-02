package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.shared.exceptions.UncheckedInterruptedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * it is used when a player selects two LeaderCard before game start
 */
public class DiscardLeaderCardController  {


    @FXML
    private ImageView imCard1;
    @FXML
    private ImageView imCard2;
    @FXML
    private ImageView imCard3;
    @FXML
    private ImageView imCard4;
    @FXML
    private Text title;

    private int result=0;
    private List<Integer> cardIds = new ArrayList<>();
    private SimpleModel game;


    /**
     * the selected card id will be send to GUIView and so the selected card will be discard
     * @param mouseEvent
     */
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

    /**
     * This method iterates and shows all LeaderCard assigned to playing player
     *
     * @param game is a mini GameController that contains all game State info
     */
    public void setGame(SimpleModel game) {
        this.game = game;
        GUISupport.setFont(33, title);
        List<Image> cards = new ArrayList<>();
        for (SimpleLeaderCard card : game.getThisPlayer().getLeaderCards()) {
            int cardId = card.getId();
            cardIds.add(cardId);
            String imUrl = "/images/leader/Leader-" + cardId + ".jpg";
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
                throw new UncheckedInterruptedException();
            }
        }
        return result;
    }
}
