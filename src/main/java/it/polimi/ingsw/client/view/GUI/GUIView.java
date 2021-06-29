package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.BuyAndAddCardInSlotMsg;
import it.polimi.ingsw.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.messages.command.PlayLeaderCardMsg;
import it.polimi.ingsw.messages.command.*;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
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

import java.net.URL;
import java.util.*;

public class GUIView extends Application implements View  {

    private SimpleModel game;
    private FXMLLoader currentLoader;
    private ServerHandler serverHandler;
    public static Font font;
    private Stage initStage;
    private Stage oldStage;
    private Alert alert;
    private Stage errorStage;
    private Stage mainStage;
    private Stage tmpStage;
    private Stage endStage=null;
    private int discardCounter = 0;
    private int marbleCounter = 0;
    private Marble marble;
    private boolean firstMain = true;
    private Action action = Action.INIT;
    private MainSceneController mainSceneController = null;
    private String serverIp;
    private int port;
    private boolean victory = false;


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
            if(settingGameController!=null)
            {
                serverIp=settingGameController.getServerIP();
                port=Integer.parseInt(settingGameController.getPort());
            }
            Socket server = new Socket(serverIp, port);
            serverHandler = new ServerHandler(server, this);
            new Thread(serverHandler).start();

            if(settingGameController!=null)settingGameController.setTextError("You are connected to the server!");

        }catch(Exception e)
        {
            if(settingGameController!=null)settingGameController.setTextError("Server unreachable, try again.");
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

   // public static void main(String[] args) { launch(args); }


    @Override
    public void startConnection() {
        Application.launch();
    }



    @Override
    public CommandMsg selectMove(boolean postTurn){
        if(firstMain){
            setLoader("/mainScene.fxml");
            Scene scene = loadScene(currentLoader);
            mainSceneController = currentLoader.getController();
            mainSceneController.setScene(game);
            if(game.getPlayers().size()==1) mainSceneController.setSinglePlayerGame();
            Platform.runLater(()->{
                if(initStage.isShowing()) initStage.close();
                if(endStage!=null) endStage.close();
                mainStage = new Stage();
                manageStage(mainStage, scene, "Main", false);
            });
            firstMain = false;
        }

        if(game.getPlayers().size()==1) mainSceneController.setRival();
            else mainSceneController.setUsernameShow();

        mainSceneController.showBasePower(false);
        mainSceneController.disableButtons(false);
        mainSceneController.setActionButton(postTurn);
        mainSceneController.setTiles();
        mainSceneController.setFaithTrack();
        mainSceneController.disableLeaderCards(true);

        if(action.equals(Action.DISCARD_LEADER) || action.equals(Action.PLAY_LEADER)) mainSceneController.setLeaderCard();
        if(action.equals(Action.BUY_RESOURCES)){
            if(tmpStage!=null&&tmpStage.isShowing())
                Platform.runLater(() ->{
                    tmpStage.close();
                });
            mainSceneController.setLeaderCard();
            mainSceneController.setWarehouse();
            mainSceneController.setMarketBoard();
        }
        if(action.equals(Action.BUY_CARD)){
            mainSceneController.setLeaderCard();
            mainSceneController.setDecks();
            mainSceneController.setWarehouse();
            mainSceneController.setSlots();
            mainSceneController.setFaithTrack();
            mainSceneController.setStrongbox();
        }
        if(action.equals(Action.END_TURN)){
            mainSceneController.setDecks();
            mainSceneController.setMarketBoard();
            mainSceneController.setFaithTrack();
        }
        if(action.equals(Action.ACTIVE_PRODUCTION))
        {
            mainSceneController.removeEffects();
            mainSceneController.setWarehouse();
            mainSceneController.setFaithTrack();
            mainSceneController.setStrongbox();
        }
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
                mainSceneController.disableButtons(true);
                int buffer = mainSceneController.getBufferId();
                boolean isRow = false;
                if(buffer > 4 && buffer < 8 ){
                    isRow = true;
                    buffer -= 4;
                }
                action = Action.BUY_RESOURCES;
                return new BuyResourcesMsg(buffer-1, isRow);
            }
            case 4->{
                action=Action.BUY_CARD;
                return buyCard();
            }
            case 5->{
                action=Action.ACTIVE_PRODUCTION;
                mainSceneController.showConfirmButton(true);
                return activateProduction(new HashSet<>(), new HashMap<>());
            }
            case 6 ->{
                mainSceneController.disableButtons(true);
                action = Action.END_TURN;
                return new EndTurnMsg();
            }

            case 7 ->{
                return show(mainSceneController.getUser(), postTurn);
            }
        }
        return null;
    }


    private CommandMsg show(String username, boolean postTurn){
        setLoader("/show.fxml");
        Scene scene = loadScene(currentLoader);
        ShowController showController = currentLoader.getController();

        showController.setGame(game, username);
        showController.setWareHouse();
        showController.setLeaderCard();
        showController.setSlots();
        showController.setStrongBox();
        showController.setTrack();
        Platform.runLater(() ->{
            if(tmpStage == null)  tmpStage = new Stage();
            manageStage(tmpStage, scene, "Show PlayerBoard", false);
        });

        boolean close = showController.isCloseWindow();
        if(close) {
            Platform.runLater(() -> {
                tmpStage.close();
            });
        }
        return selectMove(postTurn);
    }

    /**
     * Choice=1 when confirm button is pressed, choice=2 when a card is selected, choice=3 when base power is selected,
     * choice=4 when leader card1 production power is selected, choice=5 when leader card 2 production power is selected
     * @param selectedCardIds
     * @param selectedExtraPowers
     * @return
     */
    private CommandMsg activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers)
    {
        mainSceneController.disableCardsInSlot(false);
        mainSceneController.disableLeaderCards(false);
        int choice= mainSceneController.getChoice();
        switch (choice)
        {
            case 1 -> {
                if (!selectedCardIds.isEmpty() || !selectedExtraPowers.isEmpty()){
                    mainSceneController.disableSlots(true);
                    mainSceneController.showConfirmButton(false);
                    return new ActivateProductionMsg(selectedCardIds, selectedExtraPowers);
                }
                else return activateProduction(selectedCardIds, selectedExtraPowers);
            }
            case 2-> {
                selectedCardIds.add(mainSceneController.getCardId());
                return activateProduction(selectedCardIds, selectedExtraPowers);
            }
            case 3->{
                setLoader("/BasePower.fxml");
                Scene scene = loadScene(currentLoader);
                BasePowerController controller=currentLoader.getController();
                controller.setPower(game.getThisPlayer().getExtraProductionPowers().get(0));
                Platform.runLater(() ->{
                    if(tmpStage == null)  tmpStage = new Stage();
                    manageStage(tmpStage, scene, "Base Power", false);
                });
                Resource input1=controller.getChoice();
                controller.setResIn1(input1);
                Map<Resource, Integer> realInput = new HashMap<>();
                Map<Resource, Integer> realOutput = new HashMap<>();
                realInput.put(input1, 1);
                Resource input2 = controller.getChoice();
                controller.setResIn2(input2);
                if(input1.equals(input2)) realInput.put(input1, 2);
                else realInput.put(input2, 1);
                Resource output = controller.getChoice();
                realOutput.put(output, 1);
                selectedExtraPowers.put(0, new ProductionPower(realInput, realOutput));
                Platform.runLater(()->{
                    tmpStage.close();
                });
                mainSceneController.showBasePower(false);
                return activateProduction(selectedCardIds, selectedExtraPowers);
            }
            case 4 ->{
                mainSceneController.showLeaderResources(true);
                int cardId= mainSceneController.getCardId();
                Resource resource = null;
                boolean active = false;
                for(SimpleLeaderCard card : game.getThisPlayer().getLeaderCards()){
                    if(card.getId()==cardId){
                        resource=card.getAbility().getResource();
                        active=card.isActive();
                    }
                }
                if(active) {
                    int index = 0;
                    int i = 0;
                    for (ProductionPower pp : game.getThisPlayer().getExtraProductionPowers()) {
                        if (pp.getInput().containsKey(resource)) index = i;
                        i++;
                    }
                    int res = mainSceneController.getChoice();
                    switch (res) {
                        case 1 -> resource = Resource.SHIELD;
                        case 2 -> resource = Resource.STONE;
                        case 3 -> resource = Resource.SERVANT;
                        case 4 -> resource = Resource.COIN;
                    }
                    mainSceneController.showLeaderResources(false);
                    Map<Resource, Integer> realInput = new HashMap<>();
                    Map<Resource, Integer> realOutput = new HashMap<>();
                    realOutput.put(resource, 1);
                    selectedExtraPowers.put(index, new ProductionPower(realInput, realOutput));
                }
                return activateProduction(selectedCardIds, selectedExtraPowers);
            }
        }
        return null;
    }
    private CommandMsg buyCard()
    {
        mainSceneController.disableButtons(true);
        mainSceneController.disableCards(false);
        int cardId = mainSceneController.getCardId();
        mainSceneController.disableCards(true);
        mainSceneController.disableSlots(false);
        int slotId = mainSceneController.getChoice()-1;
        mainSceneController.disableSlots(true);
        SimpleDevCard card = SimpleDevCard.parse(cardId);
        return new BuyAndAddCardInSlotMsg(card.getColor(), card.getLevel(), slotId);
    }

    @Override
    public CommandMsg manageResource(){

        CommandMsg msg = null;

        setLoader("/manageResources.fxml");
        Scene scene = loadScene(currentLoader);
        ManageResourcesController manageResourcesController = currentLoader.getController();

        Platform.runLater(() ->{
            if(tmpStage == null)  tmpStage = new Stage();
            manageStage(tmpStage, scene, "Manage Resources", false);
        });

        int choice = manageResourcesController.getChoice();

        switch (choice){
            case 1 -> {
                Marble selectedMarble = selectMarble();
                if (selectedMarble == Marble.WHITE) {
                    Resource selectedResource = selectResource();
                    DepotName selectedDepot = selectDepot();
                    msg = new PutResourceMsg(selectedMarble, selectedDepot, selectedResource);
                }
                else{
                    DepotName selectedDepot = selectDepot();
                    msg = new PutResourceMsg(selectedMarble, selectedDepot);
                }
            }
            case 2 -> {
                Marble selectedMarble = selectMarble();
                msg = new DiscardMarbleMsg(selectedMarble);
            }
            case 3 ->{
                msg = changeDepots();
            }
        }

        return msg;
    }

    @Override
    public CommandMsg changeDepots() {
        setLoader("/marbleBufferScene.fxml");
        Scene scene = loadScene(currentLoader);
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.setSimpleModel(game);
        marbleBufferController.manageButton(true);
        marbleBufferController.show(false,true,false);
        marbleBufferController.setSwitchButton(true);
        marbleBufferController.changeTitle("Please select two depots to change");

        Platform.runLater(() ->{
            manageStage(tmpStage, scene, "Select Marble1", false);
        });

        DepotName depot1 = marbleBufferController.getDepot();
        DepotName depot2 = marbleBufferController.getDepot();

        if (depot1 == DepotName.FIRST_EXTRA || depot1 == DepotName.SECOND_EXTRA
                || depot2 == DepotName.FIRST_EXTRA || depot2 == DepotName.SECOND_EXTRA){
            return new MoveDepotsMsg(depot1, depot2);
        }else
            return new SwitchDepotsMsg(depot1,depot2);

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
        marbleBufferController.changeTitle("Please select the marble");
        if((marbleCounter ==0 && !game.getThisPlayer().getUsername().equals(game.getPlayers().get(0).getUsername()))
                || (marbleCounter == 1 && game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            Platform.runLater(() -> {
                oldStage = initStage;
                initStage = new Stage();
                manageStage(initStage,scene,"Select Marble", true);
            });
        }
        else if(marbleCounter >= 0 ){
            //TODO change label when player wants to discard Marble
            marbleBufferController.setMarble();
            Platform.runLater(() ->{
                manageStage(tmpStage, scene, "Select Marble1", false);
            });
        }

        marble = null;
        marble = marbleBufferController.getMarble();
        marbleCounter++;

        if((marbleCounter ==1 && !game.getThisPlayer().getUsername().equals(game.getPlayers().get(0).getUsername()))
        || (marbleCounter == 2 && game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            return Marble.WHITE;
        }
        else if(marbleCounter >= 1 ) return marble;
        return null;
    }

    @Override
    public DepotName selectDepot() {
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.show(false, true, false);
        marbleBufferController.manageButton(false);
        marbleBufferController.changeTitle("Please select where to put  resource");
        DepotName depot = marbleBufferController.getDepot();
        return depot;
    }

    @Override
    public Resource selectResource(){
        Resource resource = null;
        if((marbleCounter == 1 && !game.getThisPlayer().getUsername().equals(game.getPlayers().get(0).getUsername()))
                || (marbleCounter == 2 && game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            return marble.getResource();
        }else if(marbleCounter >= 1 ){
            MarbleBufferController marbleBufferController = currentLoader.getController();
            marbleBufferController.show(true, false, false);
            marbleBufferController.manageButton(true);
            resource = marbleBufferController.getResource();
        }

        return resource;
    }

    @Override
    public void showTextMessage(String text) {
        //URL path = getClass().getResource("/mainScene.fxml");
        if(mainSceneController!=null){
            mainSceneController.addTextInLog(text);
        }
    }


    private void setLoader(String path){
        currentLoader = new FXMLLoader(getClass().getResource(path));
    }


    private void manageStage(Stage stage, Scene scene, String title, boolean close){
        Platform.runLater(() -> {
            if(close&&oldStage!=null){
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

    public void showLeaderboard(Map<String , Integer> leaderboard){

    }

    public void endGame(){
        if(!victory) {
            setLoader("/endGame.fxml");
            Scene scene = loadScene(currentLoader);
            EndGameController controller = currentLoader.getController();
            Platform.runLater(() -> {
                oldStage = mainStage;
                manageStage(mainStage, scene, "End game", true);
            });
            int choice = controller.getChoice();
            switch (choice) {
                case 1 -> Platform.runLater(() -> mainStage.close());//endgame
                case 2 -> {
                    endStage = mainStage;
                    firstMain = true;
                    setting(null); //newGame

                }
                case 3 -> {
                    endStage = mainStage;
                    firstMain = true;
                    start(null); //new server
                }
            }
        }
    }

    @Override
    public void update(String updated) {
        if(mainSceneController!=null) {
            switch (updated) {
                case "faith" -> mainSceneController.setFaithTrack();
                case "decks" -> mainSceneController.setDecks();
                case "market" -> mainSceneController.setMarketBoard();
            }
        }
    }

    public void setModel(SimpleModel game){
        game.setView(this);
        this.game=game;
        serverHandler.setModel(game);
    }

    @Override
    public void victory(Boolean win, Map<String, Integer> leaderboard) {
        victory=true;
        setLoader("/victory.fxml");
        Scene scene = loadScene(currentLoader);
        VictoryController controller = currentLoader.getController();
        controller.setScene(win, leaderboard);
        Platform.runLater(()->{
            oldStage=mainStage;
            manageStage(mainStage, scene, "Victory", true);
        });
        Button button = (Button) currentLoader.getNamespace().get("closeButton");
        button.setOnAction(event->{
            victory=false;
            endGame();
        });
    }
}
