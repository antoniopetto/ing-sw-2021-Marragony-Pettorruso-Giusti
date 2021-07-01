package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.Marble;
import it.polimi.ingsw.shared.messages.command.CommandMsg;

import java.util.Map;

public interface View {

    void showErrorMessage(String text);
    SimpleModel getGame();
    String getUsername();
    int getNumberOfPlayers();
    void showTitle();
    void startConnection();
    CommandMsg selectMove(boolean postTurn);
    CommandMsg manageResource();
    CommandMsg changeDepots();
    CommandMsg discardLeaderCard();
    Marble selectMarble();
    DepotName selectDepot();
    Resource selectResource();
    void showTextMessage(String text, boolean loud);
    void setModel(SimpleModel game);
    void endGame();
    void update(String updated);
    void victory(Boolean win, Map<String, Integer> leaderboard);
    void endInit();
    ServerHandler getServerHandler();
}