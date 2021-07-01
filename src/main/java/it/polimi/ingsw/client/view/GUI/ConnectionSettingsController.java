package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the first scene opened when the client starts. In this scene server ip and port are asked.
 */
public class ConnectionSettingsController implements Initializable {

    private String port;
    private String serverIP;
    @FXML
    private TextField textIP;
    @FXML
    private TextField textPORT;
    @FXML
    private Label textError;
    @FXML
    private Label ipLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Button confirmButton;

    public void setPort() {
        this.port = textPORT.getText();
    }

    public void setServerIP() {
        this.serverIP = textIP.getText();
    }

    public void setTextError(String string) {
        this.textError.setText(string);
    }

    public String getPort(){
        return this.port;
    }

    public String getServerIP() {
        return serverIP;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUISupport.setFont(22, ipLabel, portLabel, textError);
        GUISupport.setFont(18, confirmButton);
        GUISupport.setFont(20, textIP, textPORT);
        textIP.setText("");
        textPORT.setText("");

    }
}
