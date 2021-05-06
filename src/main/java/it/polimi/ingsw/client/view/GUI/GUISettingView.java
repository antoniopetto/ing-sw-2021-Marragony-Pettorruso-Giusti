package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private TextField textIP;
    @FXML
    private TextField textPORT;
    @FXML
    private Label textError;
    private Object Node;

    @FXML
    void passToServer(ActionEvent event) {

        event.consume();
        String serverIP = textIP.getText();
        String port = textPORT.getText();
        try{
            guiView = new GUIView();
            server = new Socket(serverIP, Integer.parseInt(port));
            serverHandler = new ServerHandler(server, guiView, guiView.getGame());
            new Thread(serverHandler).start();

            textError.setText("You are connected to the server!");

            Parent root = FXMLLoader.load(getClass().getResource("/setGame.fxml"));
            Scene loginScene = new Scene(root);

            Stage loginWindows = (Stage) ((Node) event.getSource()).getScene().getWindow();

            loginWindows.setScene(loginScene);
            loginWindows.setResizable(false);
            loginWindows.show();


            reachable=true;
        }catch(IOException e)
        {
            textError.setText("Server unreachable, try again.");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textIP.setText("");
        textPORT.setText("");
    }
}
