package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.UncheckedInterruptedException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the window shown at the end of a game. The player has 3 choices: to end the game, to start
 * a new game on the same server or to start a new game on a different server.
 */
public class EndGameController implements Initializable {
    private int choice=0;
    @FXML
    private Text exit;
    @FXML
    private Text newGame;
    @FXML
    private Text newServer;
    @FXML
    private Text title;

    public synchronized int getChoice(){
        while (choice == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }
        int tmpChoice = choice;
        choice = 0;
        return tmpChoice;
    }

    public synchronized void setChoice(int choice){
        this.choice = choice;
        notifyAll();
    }

    /**
     * This is the event handler of the click event on one of the possible choices.
     * @param mouseEvent is the click event on the Text.
     */
    public void actionSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Text source=(Text)mouseEvent.getSource();
        String id = source.getId();
        switch (id){
            case "exit" -> setChoice(1);
            case "newGame" -> setChoice(2);
            case "newServer"-> setChoice(3);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUISupport.setFont(25, newGame, newServer, exit);
        GUISupport.setFont(40, title);
    }
}
