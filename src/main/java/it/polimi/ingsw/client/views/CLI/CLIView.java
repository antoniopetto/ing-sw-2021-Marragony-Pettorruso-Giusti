package it.polimi.ingsw.client.views.CLI;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.views.View;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.Scanner;

public class CLIView implements View {
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
    public void faceUpLeaderCard(SimplePlayer player) {

    }

    @Override
    public void showLeaderCardAllPlayers(int cardId) {

    }

    @Override
    public String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert your username:");
        System.out.println(">");
        return input.nextLine();
    }
}
