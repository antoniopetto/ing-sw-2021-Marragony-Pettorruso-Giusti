package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.BuyAndAddCardInSlotMsg;
import it.polimi.ingsw.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.messages.command.PlayLeaderCardMsg;
import it.polimi.ingsw.server.model.cards.CardParser;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIView extends Application implements View  {

    private final SimpleModel game;
    private FXMLLoader currentLoader;
    public static Font font;
    private Stage initStage;
    private Stage oldStage;
    private Alert alert;
    private Stage errorStage;
    private Stage mainStage;
    private Stage tmpStage;
    private int discardCounter = 0;
    private int marbleCounter = 0;
    private Marble marble;
    private boolean firstMain = true;
    private Action action = Action.INIT;
    private MainSceneController mainSceneController = null;


    private enum Action{
        PLAY_LEADER,
        DISCARD_LEADER,
        BUY_RESOURCES,
        END_TURN,
        BUY_CARD,
        ACTIVE_PRODUCTION,
        MOVE_DEPOT,
        INIT,
        SWITCH_DEPOT;


    }

    public GUIView() {

        game = new SimpleModel(this);
        font=Font.loadFont("@fonts/master_of_break.ttf", 14);
    }

    @Override
    public SimpleModel getGame() {
        return game;
    }

    @Override
    public void start(Stage stage) {

        setLoader("/startGame.fxml");
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/master_of_break.ttf"), 14);
        Scene scene = loadScene(currentLoader);
        Platform.runLater(() ->{
            initStage = new Stage();
            manageStage(initStage,scene, "Inserting Window", false );
        });

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
        Scene scene = loadScene(currentLoader);
        AlertController settingGameController = currentLoader.getController();
        settingGameController.setErrorLabel(text);
            openErrorStage(false, "Dialog window", scene);
            Button button = (Button) currentLoader.getNamespace().get("errorConfirm");
            button.setOnAction(event -> errorStage.close());

    }

    private void openErrorStage(boolean resizable,String title, Scene scene) {
        Platform.runLater(() -> {
            errorStage = new Stage();
            errorStage.setTitle(title);
            setScene(scene, errorStage, resizable);
            errorStage.setAlwaysOnTop(true);

        });
        Button button = (Button) currentLoader.getNamespace().get("errorConfirm");
        button.setOnAction(event -> errorStage.close());
    }

    private void setScene(Scene scene, Stage stage, boolean resizable)
    {
        Platform.runLater(()->{
            stage.setScene(scene);
            stage.setResizable(resizable);
            stage.show();
        });
    }


    @Override
    public String getUsername() {
        setLoader("/setGame.fxml");
        Scene scene = loadScene(currentLoader);
        Platform.runLater(() -> manageStage(initStage,scene, "Inserting", false ));
       StartGameController startGameController = currentLoader.getController();
       String username = startGameController.getUsername();

        return username;

    }

    @Override
    public int getNumberOfPlayers() {
        ((StartGameController) currentLoader.getController()).setUserComponents(false);
        ((StartGameController) currentLoader.getController()).setPlayersComponents(true);

        int nPlayers = ((StartGameController) currentLoader.getController()).getnPlayers();
        showErrorMessage("Waiting for other players...");
        return nPlayers;

    }

    @Override
    public void showTitle() {
        setLoader("/title.fxml");
        Scene scene = loadScene(currentLoader);
        Platform.runLater(()->{
            if(errorStage.isShowing()) errorStage.close();
            oldStage = initStage;
            initStage = new Stage();
            initStage.initStyle(StageStyle.UNDECORATED);
            initStage.setAlwaysOnTop(true);
            manageStage(initStage, scene, "", true);
        });

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
        if(firstMain){
            setLoader("/mainScene.fxml");
            Scene scene = loadScene(currentLoader);
            //TODO setStageComponents
            mainSceneController = currentLoader.getController();
            mainSceneController.setScene(game);
            Platform.runLater(()->{
                if(initStage.isShowing()) initStage.close();
                mainStage = new Stage();
                manageStage(mainStage, scene, "Main", false);
            });
            firstMain = false;
        }
        if(action.equals(Action.DISCARD_LEADER) || action.equals(Action.PLAY_LEADER)) mainSceneController.setLeaderCard();
        int choice = mainSceneController.getChoice();

        switch (choice){
            case 1 ->{
                int cardId = mainSceneController.getCardId();
                action = Action.PLAY_LEADER;
                return new PlayLeaderCardMsg(cardId);
            }
            case 2 ->{
                int cardId = mainSceneController.getCardId();
                action = Action.DISCARD_LEADER;
                return new DiscardLeaderCardMsg(cardId);
            }
            case 3 ->{
                action = Action.BUY_RESOURCES;
                return null;
            }
            case 4->{
                action=Action.BUY_CARD;
                return buyCard();
            }
        }
        return null;
    }
    private CommandMsg buyCard()
    {
        mainSceneController.disableCards(false);
        int cardId = mainSceneController.getCardId();
        mainSceneController.disableCards(true);
        mainSceneController.disableSlots(false);
        int slotId = mainSceneController.getChoice()-1;
        System.out.println(slotId);
        mainSceneController.disableSlots(true);
        SimpleCardParser parser = SimpleCardParser.getInstance();
        SimpleDevCard card = parser.getSimpleDevelopmentCard(cardId);
        return new BuyAndAddCardInSlotMsg(card.getColor(), card.getLevel(), slotId);
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

        Platform.runLater(()->{
            if(discardCounter == 0){
                oldStage = initStage;
                initStage = new Stage();
                manageStage(initStage, scene, "Discard Card Window", true);
            }
            else if(discardCounter == 1){
                manageStage(initStage, scene, "Discard Card Window", false);

            } else {
                tmpStage = new Stage();
                manageStage(tmpStage, scene, "Discard Card Window", false);
            }
        });


        int id = controller.getResult();

        discardCounter++;

        return new DiscardLeaderCardMsg(id);

    }

    @Override
    public Marble selectMarble() {

        setLoader("/marbleBufferScene.fxml");
        Scene scene = loadScene(currentLoader);
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.setSimpleModel(game);
        if(marbleCounter ==0
                || marbleCounter == 1 && (game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            Platform.runLater(() -> {
                oldStage = initStage;
                initStage = new Stage();
                manageStage(initStage,scene,"Select Marble", true);
            });
        }
        else if(marbleCounter >= 1 ){
            Platform.runLater(() ->{
                tmpStage = new Stage();
                manageStage(tmpStage, scene, "Select Marble", false);
            });
        }

        marble = null;
        marble = marbleBufferController.getMarble();
        marbleCounter++;

        if(marbleCounter ==1
        || marbleCounter == 2 && (game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            return Marble.WHITE;
        }
        else if(marbleCounter >= 2 ) return marble;

        return null;
    }

    @Override
    public void printLeaderCards(SimplePlayer player) {

    }

    @Override
    public DepotName selectDepot() {
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.show(false, true, false);
        marbleBufferController.manageButton(false);
        DepotName depot = marbleBufferController.getDepotName();
        return depot;
    }

    @Override
    public Resource selectResource(){
        Resource resource = null;
        if(marbleCounter == 1 || (marbleCounter == 2 && game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            return marble.getResource();
        }else if(marbleCounter >= 2 ){
            MarbleBufferController marbleBufferController = currentLoader.getController();
            marbleBufferController.show(true, false, false);
            marbleBufferController.manageButton(true);
            resource = marbleBufferController.getResource();
        }

        return resource;
    }

    @Override
    public void showStatusMessage(String text) {

    }


    private void setLoader(String path){
        currentLoader = new FXMLLoader(getClass().getResource(path));
    }


    private void manageStage(Stage stage, Scene scene, String title, boolean close){
        Platform.runLater(() -> {
            if(close){
                oldStage.close();
            }
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Ritagliare.png")));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);

            stage.show();
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
