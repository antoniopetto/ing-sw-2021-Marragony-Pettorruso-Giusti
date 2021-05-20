package it.polimi.ingsw.client.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartGameController implements Initializable {

    @FXML
    private TextField textUsername;
    @FXML
    private TextField textNPlayers;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nplayersLabel;

    @FXML
    public void setUsername(ActionEvent actionEvent){
        actionEvent.consume();
        GUIView.setUser(textUsername.getText());
        /*
        while(GUIView.getMessage() == null){
            usernameLabel.setText("");
        }

        usernameLabel.setText(GUIView.getMessage());*/


    }

    @FXML
    public void setNPlayers(ActionEvent actionEvent) throws IOException {
        actionEvent.consume();
        GUIView.setPlayers(Integer.parseInt(textNPlayers.getText()));
        /*
        while (GUIView.getMessage() == null){
            nplayersLabel.setText("");
        }
        nplayersLabel.setText(GUIView.getMessage());*/

        Stage newStage = new Stage();
        Stage loginWindows = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/initializegame.fxml"));
        Scene loginScene = new Scene(root);
        newStage.setScene(loginScene);
        newStage.setResizable(false);
        newStage.show();
        loginWindows.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textUsername.setText("");
        textNPlayers.setText("");
        usernameLabel.setText("");
        nplayersLabel.setText("");
    }
}
