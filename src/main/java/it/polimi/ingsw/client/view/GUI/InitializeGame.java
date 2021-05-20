package it.polimi.ingsw.client.view.GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;



public class InitializeGame extends Application {
    /*
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/initializegame.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/master_of_break.ttf"), 14);
        stage.setTitle("Schermata Iniziale");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }*/

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/master_of_break.ttf"), 14);
        stage.setTitle("Schermata Iniziale");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

    }


}
