package it.polimi.ingsw.client.view.GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartGameController implements Initializable {

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

    @FXML
    public void userConfirm(ActionEvent actionEvent){
        actionEvent.consume();
        setUsername(usernameField.getText());
    }

    @FXML
    public void playersConfirm(ActionEvent mouseEvent){
        mouseEvent.consume();
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
        usernameField.setVisible(visible);
        userLabel.setVisible(visible);
        userButton.setVisible(visible);
    }

    public void setPlayersComponents(boolean visible){
        choicePlayers.setVisible(visible);
        playersLabel.setVisible(visible);
        playersButton.setVisible(visible);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setText("");
        choicePlayers.setItems(FXCollections.observableArrayList(1,2,3,4));
        choicePlayers.setVisible(false);
        playersLabel.setVisible(false);
        playersButton.setVisible(false);
    }
}
