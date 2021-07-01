package it.polimi.ingsw.client.view.GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller of the window that opens after the connection with the server. In this window, username and number
 * of players are asked.
 */
public class GameSettingsController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox<Integer> choicePlayers;
    @FXML
    private Label userLabel;
    @FXML
    private Label playersLabel;
    @FXML
    private Button playersButton;
    @FXML
    private Button userButton;

    private String username = "";
    private int nPlayers = 0;

    public synchronized String getUsername() {
        while (username.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    private synchronized void setUsername(String username) {
        this.username = username;
        notifyAll();
    }

    /**
     * This is the event handler of the click event on the confirm button after the username has been inserted.
     * @param actionEvent is the click event.
     */
    @FXML
    public void userConfirm(ActionEvent actionEvent){
        actionEvent.consume();
        setUsername(usernameField.getText());
    }

    /**
     * This is the event handler of the click event on the confirm button after the number of players has been inserted.
     * @param actionEvent is the click event.
     */
    @FXML
    public void playersConfirm(ActionEvent actionEvent){
        actionEvent.consume();
        setnPlayers(choicePlayers.getValue()==null ? -1 : choicePlayers.getValue() );
    }

    public synchronized int getnPlayers() {
        while (nPlayers == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int tmpPlayers = nPlayers;
        nPlayers = 0;
        return tmpPlayers;
    }

    public synchronized void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
        notifyAll();
    }

    public void setUserComponents(boolean visible){
        GUISupport.setVisible(visible, usernameField, userLabel, userButton);
    }

    public void setPlayersComponents(boolean visible){
        GUISupport.setVisible(visible, choicePlayers, playersLabel, playersButton);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setText("");
        choicePlayers.setItems(FXCollections.observableArrayList(1,2,3,4));
        GUISupport.setVisible(false, choicePlayers, playersLabel, playersButton);
    }
}
