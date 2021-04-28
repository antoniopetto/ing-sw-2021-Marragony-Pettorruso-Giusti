package it.polimi.ingsw.client;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.server.model.shared.Marble;

public class View {


    public void positionUpdate(SimplePlayer player){}
    public void bufferUpdate(Marble marble){}
    public void showMessage(String text){}
    public void faceUpLeaderCard(SimplePlayer player){}
    public void showLeaderCardAllPlayers(int cardId){}
}
