package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

public class GUIView extends Application implements View  {


    private final SimpleGame game;
    private String username;
    private boolean loop = true;
    private int nPlayers;
    private FXMLLoader currentLoader;
    public static Font font;
    private Stage currentStage;
    private Stage oldStage;

    public GUIView() {

        game = new SimpleGame(this);
        font=Font.loadFont("@fonts/master_of_break.ttf", 14);
    }

    @Override
    public SimpleGame getGame() {
        return game;
    }

    @Override
    public void start(Stage stage) {

        setLoader("/initializegame.fxml");
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/master_of_break.ttf"), 14);
        manageStage(false, "Inserting", true, loadScene(currentLoader), false);

        SettingGameController settingGameController = currentLoader.getController();
       Button button = (Button) currentLoader.getNamespace().get("confirmButton");
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

        }catch(IOException e)
        {
            settingGameController.setTextError("Server unreachable, try again.");
        }
    }

    @Override
    public void showErrorMessage(String text) {

        setLoader("/alertDialog.fxml");

        AlertController settingGameController = currentLoader.getController();
        settingGameController.setErrorLabel(text);


            manageStage(false, "Error Message", true, loadScene(currentLoader), false);
            Button button = (Button) currentLoader.getNamespace().get("errorConfirm");
            button.setOnAction(event ->{
                currentStage.close();
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

       setLoader("/setGame.fxml");
        manageStage(false, "Start Config", false, loadScene(currentLoader), false);


        StartGameController startGameController = currentLoader.getController();
        Button button = (Button) currentLoader.getNamespace().get("confirm");
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
        ((StartGameController )currentLoader.getController()).setUsernameField(false);
        ((StartGameController )currentLoader.getController()).setPlayersLabel(true);
        ((StartGameController )currentLoader.getController()).setUsernameLabel(false);
        ((StartGameController )currentLoader.getController()).setChoicePlayers(true);
        Button button = (Button) currentLoader.getNamespace().get("confirm");
        loop = true;
        while(loop) {
            button.setOnAction(event -> {
                setPlayers( ((StartGameController )currentLoader.getController()).getChoicePlayers());
                setLoop(false);
            });
        }

        return nPlayers;

    }

    @Override
    public void showTitle() {

    }


    @Override
    public void showMarbleBuffer(List<Marble> marbleList) {

    }

    public static void main(String[] args)
    {
        launch(args);
    }


    @Override
    public void startSetting() {

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
        setLoader("/setUp.fxml");
        Scene scene = loadScene(currentLoader);
        setUpController controller = currentLoader.getController();
        controller.setGame(game);
        manageStage(false, "Select Cards", true, scene, true);

        int id = controller.selectedCard(null);
        while(id==0)
        {
            id = controller.selectedCard(null);
        }
        DiscardLeaderCardMsg msg = new DiscardLeaderCardMsg(id);
        return msg;
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


    private void setLoader(String path){
        currentLoader = new FXMLLoader(getClass().getResource(path));
    }

    private void manageStage(boolean resizable, String title, boolean createStage, Scene scene, boolean close){
        oldStage = currentStage;
        Platform.runLater(() -> {

            if(createStage){
                currentStage = new Stage();
                currentStage.setTitle(title);
            }
            currentStage.setScene(scene);
            currentStage.setResizable(resizable);
            if(close) oldStage.close();
            currentStage.show();
        });

    }


    private Scene loadScene(FXMLLoader loader){
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene(root);
    }

}
