package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.util.List;
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
}