package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this controller is used to bring up the alert dialog with a message
 */
public class AlertController implements Initializable {

    @FXML
    private Label errorLabel;

    public void setErrorLabel(String errorLabel) {
        this.errorLabel.setText(errorLabel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
    }
}
