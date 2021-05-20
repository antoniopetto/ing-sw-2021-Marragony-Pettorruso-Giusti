package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.util.List;

public interface View {

    void showErrorMessage(String text);
    void showConfirmMessage(String text);
    SimpleGame getGame();
    void showMarbleBuffer(List<Marble> marbleList);
    String getUsername();
    int getNumberOfPlayers();
    void showTitle();
    void startSetting();
    CommandMsg selectMove(boolean postTurn);
    CommandMsg manageResource();
    void showLeaderCard(SimpleLeaderCard card);
    CommandMsg discardLeaderCard();
    Marble selectMarble();
    void printLeaderCards(SimplePlayer player);
    DepotName selectDepot();
    Resource selectResource();
    void showStatusMessage(String text);

}
