package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class GUIView extends Application implements View  {


    private final SimpleGame game;
    private Stage loginWindows;
    private String username;
    private boolean loop = true;
    private int nPlayers;
    private StartGameController startGameController;
    private FXMLLoader loader;


    public GUIView() {
        game = new SimpleGame(this);
    }

    @Override
    public SimpleGame getGame() {
        return game;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/initializegame.fxml"));

        root = loader.load();
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/master_of_break.ttf"), 14);
        stage.setTitle("Schermata Iniziale");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        SettingGameController settingGameController = loader.getController();
        loginWindows = stage;
       Button button = (Button) loader.getNamespace().get("confirmButton");
            button.setOnAction(event -> {
                settingGameController.setPort();
                settingGameController.setServerIP();
                setting(settingGameController);
    });

    }



    public void setting(SettingGameController settingGameController){
        try{
            Socket server = new Socket(settingGameController.getServerIP(), Integer.parseInt(settingGameController.getPort()));
            ServerHandler serverHandler = new ServerHandler(server, this, this.getGame());
            new Thread(serverHandler).start();

            settingGameController.setTextError("You are connected to the server!");

            boolean reachable = true;
        }catch(IOException e)
        {
            settingGameController.setTextError("Server unreachable, try again.");
        }
    }

    @Override
    public void showErrorMessage(String text) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alertDialog.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AlertController settingGameController = loader.getController();
        settingGameController.setErrorLabel(text);
        Scene scene = new Scene(root);
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setTitle("Error Message");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            Button button = (Button) loader.getNamespace().get("errorConfirm");
            button.setOnAction(event ->{
                stage.close();
            });
        });


    }







    private void setUsername(String username) {
        this.username = username;
    }

    private void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public String getUsername() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/setGame.fxml"));
        setLoader(loader);
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene loginScene = new Scene(root);

        Platform.runLater(() -> {
            loginWindows.setScene(loginScene);
            loginWindows.setResizable(false);
            loginWindows.show();
        });

        startGameController = loader.getController();
        Button button = (Button) loader.getNamespace().get("confirm");
        loop = true;
    while(loop) {
            button.setOnAction(event -> {
            setUsername(startGameController.getUsernameField());
            setLoop(false);
            });
        }
        return username;

    }

    private void setPlayers(int nPlayers){
        this.nPlayers = nPlayers;
    }

    @Override
    public int getNumberOfPlayers() {
        startGameController.setUsernameField(false);
        startGameController.setPlayersLabel(true);
        startGameController.setUsernameLabel(false);
        startGameController.setChoicePlayers(true);
        Button button = (Button) loader.getNamespace().get("confirm");
        loop = true;
        while(loop) {
            button.setOnAction(event -> {
                setPlayers(startGameController.getChoicePlayers());
                setLoop(false);
            });
        }
        return nPlayers;

    }

    @Override
    public void showTitle() {
        Platform.runLater(() -> {
            loginWindows.close();
        });
    }


    @Override
    public void showMarbleBuffer(List<Marble> marbleList) {

    }


    @Override
    public void startSetting() {
        Application.launch(GUIView.class);
    }



    @Override
    public CommandMsg selectMove(boolean postTurn){
        return null;
    }

    @Override
    public CommandMsg manageResource(){
        return null;
    }
    @Override
    public void showLeaderCard(SimpleLeaderCard card) {

    }

    @Override
    public CommandMsg discardLeaderCard() {
        return null;
    }

    @Override
    public Marble selectMarble() {
        return null;
    }

    @Override
    public void printLeaderCards(SimplePlayer player) {

    }

    @Override
    public DepotName selectDepot() {
        return DepotName.HIGH;
    }

    public Resource selectResource(){ return Resource.COIN;}

    @Override
    public void showStatusMessage(String text) {

    }


    private void setLoader(FXMLLoader loader){
        this.loader = loader;
    }

}
