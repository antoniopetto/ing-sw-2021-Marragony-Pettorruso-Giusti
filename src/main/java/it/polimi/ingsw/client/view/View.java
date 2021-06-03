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
    void showMarbleBuffer(List<Marble> marbleList);
    String getUsername();
    int getNumberOfPlayers();
    void showTitle();
    void startConnection();
    CommandMsg selectMove(boolean postTurn);
    CommandMsg manageResource();
    void showLeaderCard(SimpleLeaderCard card);
    CommandMsg discardLeaderCard();
    Marble selectMarble();
    void printLeaderCards(SimplePlayer player);
    DepotName selectDepot();
    Resource selectResource();
    void showTextMessage(String text);
    void showLeaderboard(Map<String, Integer> leaderboard);
    void setModel(SimpleModel game);
    void endGame();
}
