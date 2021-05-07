package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;

public interface View {


    void positionUpdate(SimplePlayer player);
    void bufferUpdate(Marble marble);
    void showErrorMessage(String text);
    void showConfirmMessage(String text);
    void faceUpLeaderCard(SimplePlayer player, int cardId);
    void discardLeaderCard(SimplePlayer player, int cardId);
    void showLeaderCardAllPlayers(int cardId);
    String getUsername();
    int getNumber();
    void startGame();
    void startSetting();
    void showDevCardAllPlayers(int cardId);
    void addCardInSlot(SimplePlayer player, int cardId, int cardSlot);
    CommandMsg selectMove();
}
