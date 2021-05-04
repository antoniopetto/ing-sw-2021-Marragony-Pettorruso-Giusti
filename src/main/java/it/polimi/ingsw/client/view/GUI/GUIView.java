package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.shared.messages.command.CommandMsg;
import javafx.application.Application;

public class GUIView implements View {

    private InitializeGame initializeGame;

    public GUIView() {
        this.initializeGame = new InitializeGame();
    }

    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showMessage(String text) {

    }

    @Override
    public void faceUpLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void discardLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void showLeaderCardAllPlayers(int cardId) {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public void startGame() {
    }

    @Override
    public void startSetting() {
        Application.launch(initializeGame.getClass());
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

}
