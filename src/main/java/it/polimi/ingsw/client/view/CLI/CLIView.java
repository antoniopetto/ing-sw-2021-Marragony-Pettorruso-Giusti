package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.Scanner;

public class CLIView implements View {
    private CLISettingView settingView;

    public CLIView() {
        this.settingView = new CLISettingView(this);
    }

    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showMessage(String text) {
        System.out.println(Graphics.ANSI_RED+"ATTENTION!");
        System.out.println(text);
    }

    @Override
    public void faceUpLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void discardLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void showLeaderCardAllPlayers(int cardId) {

    }

    @Override
    public String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert your username:");
        System.out.print(Graphics.ANSI_CYAN+">");
        return input.nextLine();
    }

    public int getNumber(){
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert the number of players for your game:");
        System.out.print(Graphics.ANSI_CYAN+">");
        return input.nextInt();
    }

    @Override
    public void startGame() {
        System.out.println(Graphics.ANSI_GREEN+Graphics.TITLE);
        System.out.println("Game started");
    }

    @Override
    public void startSetting() {
        settingView.execute();
    }

    @Override
    public void showDevCardAllPlayers(int cardId) {

    }

    @Override
    public void addCardInSlot(SimplePlayer player, int cardId, int cardSlot) {

    }


}
