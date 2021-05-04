package it.polimi.ingsw.client.view.GUI;

import com.sun.prism.Image;
import it.polimi.ingsw.client.ServerHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class GUISettingView implements Initializable {

    private GUIView guiView;
    private Socket server;
    private ServerHandler serverHandler;
    private boolean reachable = false;
    @FXML
    private TextField textfield1;
    @FXML
    private TextField textfield2;
    @FXML
    private Label textError;

    @FXML
    void passToServer(ActionEvent event) {

        event.consume();
        String serverIP = textfield1.getText();
        String port = textfield2.getText();
        try{
            guiView = new GUIView();
            server = new Socket(serverIP, Integer.parseInt(port));
            serverHandler = new ServerHandler(server, guiView);
            textError.setText("You are connected to the server!");
            new Thread(serverHandler).start();
            reachable=true;
        }catch(IOException e)
        {
            textError.setText("Server unreachable, try again.");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textfield1.setText("");
        textfield2.setText("");
    }
}
