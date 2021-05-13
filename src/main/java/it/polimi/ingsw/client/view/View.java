package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
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
    SimpleGame getGame();
    void showMarbleBuffer(List<Marble> marbleList);
    String getUsername();
    int getNumber();
    void startGame();
    void startSetting();
    CommandMsg selectMove(boolean postTurn);
    void showLeaderCard(SimpleLeaderCard card);
    CommandMsg discardLeaderCard();
    Marble selectedMarble();
    void printLeaderCards(SimplePlayer player);
    int selectedDepot();
    void showStatusMessage(String text);

}
