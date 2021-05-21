package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingGameController implements Initializable {


    private String port;
    private String serverIP;
    @FXML
    private TextField textIP;
    @FXML
    private TextField textPORT;
    @FXML
    private Label textError;


    public void setPort() {
        this.port = textPORT.getText();;
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
        textIP.setText("");
        textPORT.setText("");
        textIP.setFont(GUIView.font);
    }
}
