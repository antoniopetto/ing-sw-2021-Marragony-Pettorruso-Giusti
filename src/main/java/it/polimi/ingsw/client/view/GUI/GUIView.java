package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;
import javafx.application.Application;

import java.util.List;

public class GUIView implements View {

    private InitializeGame initializeGame;
    private SimpleGame game;

    public GUIView() {
        this.initializeGame = new InitializeGame();
        game = new SimpleGame(this);
    }

    public SimpleGame getGame() {
        return game;
    }

    private  static volatile String username = null;
    private  static volatile int nPlayers = 0;
    private static String Message = null;

    public static String getMessage(){

        if(Message == null) return null;
        String message = new String(Message);
        Message = null;
        return message;
    }

    public static void setUser(String user){
        username = user;
    }

    public static void setPlayers(int players){
        nPlayers = players;
    }

    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showErrorMessage(String text) {
        Message = text;
    }

    @Override
    public void showConfirmMessage(String text) {

    }

    @Override
    public void faceUpLeaderCard(int cardId) {

    }


    @Override
    public void showMarbleBuffer(List<Marble> marbleList) {

    }

    @Override
    public String getUsername() {
        while (username == null) {
            Thread.onSpinWait();
        }
        return username;
    }

    @Override
    public int getNumber() {
        while (nPlayers == 0) {
            Thread.onSpinWait();
        }
        return nPlayers;
    }

    @Override
    public void startGame() {
        Message = "Game Started!";
    }

    @Override
    public void startSetting() {
        Application.launch(InitializeGame.class);
    }

    @Override
    public void showDevCardAllPlayers(int cardId) {

    }

    @Override
    public void addCardInSlot(SimplePlayer player, int cardId, int cardSlot) {

    }

    @Override
    public CommandMsg selectMove(){
        return null;
    }

    @Override
    public void showLeaderCard(SimpleLeaderCard card) {

    }

    @Override
    public int getDiscardedLeaderCard() {
        return 0;
    }

    @Override
    public Marble selectedMarble() {
        return null;
    }

    @Override
    public void printLeaderCard(SimplePlayer player) {

    }

    @Override
    public int selectedDepot() {
        return 0;
    }


}
