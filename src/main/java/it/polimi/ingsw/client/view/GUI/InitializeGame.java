package it.polimi.ingsw.client.view.GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;



public class InitializeGame extends Application {



    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/initializegame.fxml"));
        stage.setTitle("Schermata Iniziale");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
