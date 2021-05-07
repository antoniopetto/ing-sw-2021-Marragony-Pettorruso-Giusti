package it.polimi.ingsw.client.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        while(GUIView.getMessage() == null){
            usernameLabel.setText("");
        }
        if(GUIView.getMessage().equals(" "))
        {
            usernameLabel.setText(GUIView.getMessage());

        }
        else{

        }

    }

    @FXML
    public void setNPlayers(ActionEvent actionEvent){
        actionEvent.consume();
        GUIView.setPlayers(Integer.parseInt(textNPlayers.getText()));
        /*
        while (GUIView.getMessage() == null){
            nplayersLabel.setText("");
        }
        nplayersLabel.setText(GUIView.getMessage());*/
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textUsername.setText("");
        textNPlayers.setText("");
        usernameLabel.setText("");
        nplayersLabel.setText("");
    }
}
