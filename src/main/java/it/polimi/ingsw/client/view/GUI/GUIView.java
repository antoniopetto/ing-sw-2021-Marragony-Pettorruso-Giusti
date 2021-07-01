package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.BuyAndAddCardInSlotMsg;
import it.polimi.ingsw.shared.messages.command.DiscardLeaderCardMsg;
import it.polimi.ingsw.shared.messages.command.PlayLeaderCardMsg;
import it.polimi.ingsw.shared.messages.command.*;
import it.polimi.ingsw.shared.ProductionPower;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.Marble;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;

import java.util.*;

/**
 * This class handles the GUI with javafx, so it extends Application. It implements the interface <code>View</code>,
 * used by the messages to communicate with the Client, using the Strategy pattern.
 */
public class GUIView extends Application implements View  {

    private SimpleModel game;
    private FXMLLoader currentLoader;
    private ServerHandler serverHandler;
    public static Font font;
    private Stage initStage;
    private Stage oldStage;
    private Stage errorStage;
    private Stage mainStage;
    private Stage tmpStage;
    private int discardCounter = 0;
    private int marbleCounter = 0;
    private Marble marble;
    private boolean firstMain = true;
    private Action action = Action.INIT;
    private MainSceneController mainSceneController = null;
    private String serverIp;
    private int port;


    private enum Action{
        PLAY_LEADER,
        DISCARD_LEADER,
        BUY_RESOURCES,
        END_TURN,
        BUY_CARD,
        ACTIVE_PRODUCTION,
        INIT,
    }

    @Override
    public SimpleModel getGame() {
        return game;
    }

    /**
     * This method is used to start the GUI. It loads the first scene, where server ip and port are asked to the user.
     */
    @Override
    public void start(Stage stage) {

        setLoader("/fxml/connectionSettings.fxml");
        Scene scene = loadScene(currentLoader);
        Platform.runLater(() ->{
            if(mainStage!=null) oldStage=mainStage;
            initStage = new Stage();
            manageStage(initStage, scene, "Masters of Renaissance", true);
        });

        ConnectionSettingsController connectionSettingsController = currentLoader.getController();
        Button button = (Button) currentLoader.getNamespace().get("confirmButton");
        button.setOnAction(event -> {
            connectionSettingsController.setPort();
            connectionSettingsController.setServerIP();
            setting(connectionSettingsController);
        });
    }

    /**
     * This method is called when the client is connected with the server and username and number of players are asked.
     * @param connectionSettingsController is the controller of the scene. If it is null, it means that you are starting
     *                                     a new game after the end of an old one, so server ip and port are already saved
     *                                     in the class
     */
    public void setting(ConnectionSettingsController connectionSettingsController){
        try{
            if(connectionSettingsController != null) {
                serverIp= connectionSettingsController.getServerIP();
                port=Integer.parseInt(connectionSettingsController.getPort());
            }
            Socket server = new Socket(serverIp, port);
            serverHandler = new ServerHandler(server, this);
            new Thread(serverHandler).start();

            if(connectionSettingsController !=null) connectionSettingsController.setTextError("You are connected to the server!");
        }catch(Exception e) {
            if(connectionSettingsController !=null) connectionSettingsController.setTextError("Server unreachable, try again.");
        }
    }

    /**
     * This method is used to show an error message.
     * @param text is the text of the message
     */
    @Override
    public void showErrorMessage(String text){
        showMessage(text, true);
    }

    /**
     * This method is used to show a message.
     * @param text is the text of the message
     * @param closable tells if the user can close the scene or it is a waiting massage that can't be closed
     */
    public void showMessage(String text, boolean closable) {

        setLoader("/fxml/alertDialog.fxml");
        Scene scene = loadScene(currentLoader);
        AlertController settingGameController = currentLoader.getController();
        settingGameController.setErrorLabel(text);
        Platform.runLater(() -> {
            if (errorStage == null || !errorStage.isShowing()){
                errorStage = new Stage();
                errorStage.setTitle("Dialog window");
            }
            setScene(scene, errorStage, false);
            errorStage.setAlwaysOnTop(true);
        });
        Button button = (Button) currentLoader.getNamespace().get("errorConfirm");
        button.setOnAction(event -> errorStage.close());
        button.setVisible(closable);
    }

    private void setScene(Scene scene, Stage stage, boolean resizable) {
        Platform.runLater(()->{
            stage.setScene(scene);
            stage.setResizable(resizable);
            stage.show();
        });
    }

    /**
     * This method is called when the username is asked to the user.
     * @return the username chosen by the user.
     */
    @Override
    public String getUsername() {
        setLoader("/fxml/gameSettings.fxml");
        Scene scene = loadScene(currentLoader);
        Platform.runLater(() -> {
            if(mainStage!=null) oldStage=mainStage;
            manageStage(initStage,scene, "Masters of Renaissance", true );
        });
        GameSettingsController startGameController = currentLoader.getController();
        String username = startGameController.getUsername();

        return username;
    }
    /**
     * This method is called when the number of players is asked to the user.
     * @return the number chosen by the user.
     */
    @Override
    public int getNumberOfPlayers() {
        ((GameSettingsController) currentLoader.getController()).setUserComponents(false);
        ((GameSettingsController) currentLoader.getController()).setPlayersComponents(true);

        int nPlayers = ((GameSettingsController) currentLoader.getController()).getnPlayers();
        showMessage("Waiting for other players...", false);
        return nPlayers;
    }

    /**
     * This method closes the init stage to start the game.
     */
    @Override
    public void showTitle() {
        closeStage(initStage);
    }

    @Override
    public void startConnection() {
        Application.launch();
    }

    /**
     * This method is used when the initialization phase ends and the main scene is showed to all the players.
     */
    public void endInit(){

        setLoader("/fxml/mainScene.fxml");
        Scene scene = loadScene(currentLoader);
        mainSceneController = currentLoader.getController();
        mainSceneController.setScene(game);
        if(game.getPlayers().size()==1)
            mainSceneController.setSinglePlayerGame();
        Platform.runLater(()->{
            closeStage(errorStage, initStage);
            mainStage = new Stage();
            manageStage(mainStage, scene, "Masters of Renaissance", false);
        });
        firstMain = false;

        if(game.getPlayers().size()==1) mainSceneController.setRival();
        else mainSceneController.setUsernameShow();

        mainSceneController.showBasePower(false);
        mainSceneController.disableButtons(true);
        mainSceneController.setActionButton(false);
        mainSceneController.setTiles();
        mainSceneController.setFaithTrack();
        mainSceneController.disableLeaderCards(true);
    }

    /**
     * This method is called by <code>NextActionMsg</code> when a move is asked to the user. Based on the last action done,
     * it uploads some parts of the main scene. The first if is used when the main scene is opened for the first time.
     * @param postTurn tells if the players is at the beginning of the turn or it has already done a move so it can
     *                 only do a leader card action or end the turn
     * @return the message of the chosen move to the server.
     */
    @Override
    public CommandMsg selectMove(boolean postTurn){
        if(firstMain){
            setLoader("/fxml/mainScene.fxml");
            Scene scene = loadScene(currentLoader);
            mainSceneController = currentLoader.getController();
            mainSceneController.setScene(game);
            if(game.getPlayers().size()==1)
                mainSceneController.setSinglePlayerGame();
            Platform.runLater(()->{
                closeStage(initStage, errorStage);
                mainStage = new Stage();
                manageStage(mainStage, scene, "Masters of Renaissance", false);
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
            closeStage(tmpStage);
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

    /**
     * This method is used to show the playerBoard of the other players.
     * @param username is the username of the player the user wants to show
     * @param postTurn is used when selectMove is called again
     * @return the same message returned by <code>selectMove</code>
     */
    private CommandMsg show(String username, boolean postTurn){
        setLoader("/fxml/show.fxml");
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
            tmpStage.initStyle(StageStyle.UNDECORATED);
            tmpStage.setAlwaysOnTop(true);
            manageStage(tmpStage, scene, "Show PlayerBoard", false);
        });

        if(showController.isCloseWindow()) {
            closeStage(tmpStage);
        }
        return selectMove(postTurn);
    }

    /**
     * This method is used when the player wants to activate the production. There are several actions that the player can do.
     * Choice=1 when confirm button is pressed, choice=2 when a card is selected, choice=3 when base power is selected,
     * choice=4 when leader card1 production power is selected, choice=5 when leader card 2 production power is selected
     * @param selectedCardIds contains the ids of the cards selected to activate the production power.
     * @param selectedExtraPowers contains the extra production powers (base power and leader card powers) selected by the pleyer.
     * @return an <code>ActivateProductionMsg</code>
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
                setLoader("/fxml/basePower.fxml");
                Scene scene = loadScene(currentLoader);
                BasePowerController controller=currentLoader.getController();
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
                closeStage(tmpStage);
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

    /**
     * This method is called when the buy card action is selected. It asks the user the color and level of the card to
     * buy and the id of the slot where to put the card
     * @return a <code>BuyAndAddCardInSlotMsg</code>
     */
    private CommandMsg buyCard() {
        mainSceneController.disableButtons(true);
        mainSceneController.disableCards(false);
        int cardId = mainSceneController.getCardId();
        System.out.println(cardId);
        mainSceneController.disableCards(true);
        mainSceneController.disableSlots(false);
        int slotId = mainSceneController.getChoice()-1;
        mainSceneController.disableSlots(true);
        SimpleDevCard card = SimpleDevCard.parse(cardId);
        return new BuyAndAddCardInSlotMsg(card.getColor(), card.getLevel(), slotId);
    }

    /**
     * This method is used when the player buys marbles from the marketBoard, so it has to chose a resource action:
     * put a resource, discard a marble or change depots.
     * @return the message of the action chosen by the player.
     */
    @Override
    public CommandMsg manageResource(){

        CommandMsg msg = null;

        setLoader("/fxml/manageResources.fxml");
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
            case 3 ->msg = changeDepots();
        }
        return msg;
    }

    /**
     * This method is used when the player wants to change two depots in the warehouse.
     * @return a <code>MoveDepotsMsg</code> or a <code>SwitchDepotsMsg</code>
     */
    @Override
    public CommandMsg changeDepots() {
        setLoader("/fxml/marbleBufferScene.fxml");
        Scene scene = loadScene(currentLoader);
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.setSimpleModel(game);
        marbleBufferController.manageButton(true);
        marbleBufferController.show(false,true,false);
        marbleBufferController.setSwitchButton(true);
        marbleBufferController.changeTitle("Please select two depots to change");

        Platform.runLater(() -> manageStage(tmpStage, scene, "Select Marble1", false));

        DepotName depot1 = marbleBufferController.getDepot();
        DepotName depot2 = marbleBufferController.getDepot();

        if (depot1 == DepotName.FIRST_EXTRA || depot1 == DepotName.SECOND_EXTRA
                || depot2 == DepotName.FIRST_EXTRA || depot2 == DepotName.SECOND_EXTRA){
            return new MoveDepotsMsg(depot1, depot2);
        } else
            return new SwitchDepotsMsg(depot1,depot2);
    }

    /**
     * This method is used at the beginning of a game when each player has to discard two of the four leader cards shown.
     * @return a <code>DiscardLeaderCardMsg</code> with the card to discard.
     */
    @Override
    public CommandMsg discardLeaderCard() {
        setLoader("/fxml/discardLeaderCard.fxml");
        Scene scene = loadScene(currentLoader);
        DiscardLeaderCardController controller = currentLoader.getController();
        controller.setGame(game);

        Platform.runLater(()->{
            if(discardCounter == 0){
                oldStage = errorStage;
                initStage = new Stage();
                manageStage(initStage, scene, "Discard Leader Card", true);
            }
            else if(discardCounter == 1){
                manageStage(initStage, scene, "Discard Leader Card", false);
            }
        });

        int id = controller.getResult();
        discardCounter++;
        return new DiscardLeaderCardMsg(id);
    }

    /**
     * This method shows a scene with the marbles bought, which can be inserted in the warehouse or discarded. The player
     * selects one of the marbles.
     * @return the marble selected
     */
    @Override
    public Marble selectMarble() {

        closeStage(errorStage);
        setLoader("/fxml/marbleBufferScene.fxml");
        Scene scene = loadScene(currentLoader);
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.setSimpleModel(game);
        marbleBufferController.changeTitle("Please select the marble");
        if((marbleCounter == 0 && !game.getThisPlayer().getUsername().equals(game.getPlayers().get(0).getUsername()))
                || (marbleCounter == 1 && game.getPlayers().size() == 4 && game.getThisPlayer().getUsername().equals(game.getPlayers().get(3).getUsername()))){
            Platform.runLater(() -> {
                oldStage = initStage;
                initStage = new Stage();
                manageStage(initStage, scene,"Select Marble", true);
            });
        }
        else if(marbleCounter >= 0 ){
            //TODO change label when player wants to discard Marble
            marbleBufferController.setMarble();
            manageStage(tmpStage, scene, "Select Marble1", false);
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

    /**
     * This method is used when a player is adding a resource in the warehouse, so it has to chose a depot to put the resource
     * @return the DepotName of the depot selected.
     */
    @Override
    public DepotName selectDepot() {
        MarbleBufferController marbleBufferController = currentLoader.getController();
        marbleBufferController.show(false, true, false);
        marbleBufferController.manageButton(false);
        marbleBufferController.changeTitle("Please select where to put  resource");
        DepotName depot = marbleBufferController.getDepot();
        return depot;
    }

    /**
     * This method is used when a white marble power is active so the player has to chose a resource among the resources
     * available to put in the warehouse when a white marble is bought.
     * @return the resource chosen.
     */
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

    /**
     * This method is used to show messages received by the client as <code>TextMsg</code>. They are shown in the log
     * in the main scene, or as pop up messages if the main scene hasn't been opened yet.
     * @param text is the text of the message.
     * @param loud if true the message is shown in a pop up window without the "close" button
     */
    @Override
    public void showTextMessage(String text, boolean loud) {
        //URL path = getClass().getResource("/mainScene.fxml");
        if(mainSceneController != null){
            mainSceneController.addTextInLog(text);
        }
        else if (loud)
            showMessage(text, false);
    }

    private void setLoader(String path){
        currentLoader = new FXMLLoader(getClass().getResource(path));
    }

    private void manageStage(Stage stage, Scene scene, String title, boolean close){
        Platform.runLater(() -> {
            if(close && oldStage!=null){
                oldStage.close();
            }
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/inkwell.png")));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);

            stage.show();
        });
    }

    private Scene loadScene(FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(root);
    }

    /**
     * This method is used after the end of a game, caused by the victory of a player or the disconnection of the server.
     * The user can chose to end the game and close the window, start a new game on the same server or start a new game on a
     * different server.
     */
    public void endGame(){

        setLoader("/fxml/endGame.fxml");
        Scene scene = loadScene(currentLoader);
        EndGameController controller = currentLoader.getController();
        closeAllStages();
        Platform.runLater(() -> {
            if (mainStage == null)
                mainStage = new Stage();
            manageStage(mainStage, scene, "End game", true);
        });
        int choice = controller.getChoice();
        switch (choice) {
            case 1 -> Platform.runLater(() -> mainStage.close()); //endgame
            case 2 -> {
                firstMain = true;
                setting(null); //newGame
            }
            case 3 -> {
                firstMain = true;
                start(null); //new server
            }
        }
    }


    /**
     * This method is used to update synchronously the main scene when it changes after the moves of other players.
     * @param updated is the part of the main scene that changed.
     */
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

    /**
     * This method is called when a player wins. It closes the main scene and it opens a new scene with the name of the winner
     * and the leaderboard.
     * @param win is used in single player games only, to tell either if you won or if you lost. In multiplayer games
     *            <code>win</code> is null.
     * @param leaderboard is the final leaderboard.
     */
    @Override
    public void victory(Boolean win, Map<String, Integer> leaderboard) {

        setLoader("/fxml/victory.fxml");
        Scene scene = loadScene(currentLoader);
        VictoryController controller = currentLoader.getController();
        controller.setScene(win, leaderboard);
        Platform.runLater(()->{
            oldStage = mainStage;
            manageStage(mainStage, scene, "Victory", true);
        });

        boolean close = controller.isCloseWindow();
        if(close)
            endGame();
    }

    private void closeAllStages(){
        closeStage(oldStage, mainStage, errorStage, initStage, tmpStage);
    }

    private void closeStage(Stage... stage){
        for (Stage s : stage){
            if (s != null && s.isShowing())
                Platform.runLater(s::close);
        }
    }
}
