package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.shared.Marble;

public interface View {


    void positionUpdate(SimplePlayer player);
    void bufferUpdate(Marble marble);
    void showMessage(String text);
    void faceUpLeaderCard(SimplePlayer player);
    void showLeaderCardAllPlayers(int cardId);
    String getUsername();
    int getNumber();
    void startGame();

}
