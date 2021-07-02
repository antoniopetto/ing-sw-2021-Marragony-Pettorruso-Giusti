package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this controller is used to bring up the alert dialog with a message
 */
public class AlertController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private Button errorConfirm;

    public void setErrorLabel(String errorLabel) {
        this.errorLabel.setText(errorLabel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUISupport.setFont(25, errorLabel);
        GUISupport.setFont(12, errorConfirm);
        errorLabel.setText("");
    }
}
