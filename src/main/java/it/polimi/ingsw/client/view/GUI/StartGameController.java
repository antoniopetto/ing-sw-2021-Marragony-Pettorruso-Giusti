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


    public String getUsernameField() {
        return usernameField.getText();
    }

    public void setUsernameField(boolean visible){
        usernameField.setVisible(visible);
    }
    public void setUsernameLabel(boolean visible){
        userLabel.setVisible(visible);
    }
    public void setChoicePlayers(boolean visible){
        choicePlayers.setVisible(visible);
    }
    public void setPlayersLabel(boolean visible){
        playersLabel.setVisible(visible);
    }

    public int getChoicePlayers() {
        return choicePlayers.getValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setText("");
        choicePlayers.setItems(FXCollections.observableArrayList(1,2,3,4));
        choicePlayers.setVisible(false);
        playersLabel.setVisible(false);
    }
}
