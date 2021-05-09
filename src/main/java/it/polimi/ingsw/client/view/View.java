package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.util.List;

public interface View {


    void positionUpdate(SimplePlayer player);
    void bufferUpdate(Marble marble);
    void showErrorMessage(String text);
    void showConfirmMessage(String text);
    void faceUpLeaderCard(SimplePlayer player, int cardId);
    void discardLeaderCard(SimplePlayer player, int cardId);
    void showLeaderCardAllPlayers(int cardId);
    void showMarbleBuffer(List<Marble> marbleList);
    String getUsername();
    int getNumber();
    void startGame();
    void startSetting();
    void showDevCardAllPlayers(int cardId);
    void addCardInSlot(SimplePlayer player, int cardId, int cardSlot);
    CommandMsg selectMove();
    void showLeaderCard(SimpleLeaderCard card, int counter);
    int getDiscardLeaderCard(String username);
    Marble selectedMarble();
    int selectedDepot();
}
