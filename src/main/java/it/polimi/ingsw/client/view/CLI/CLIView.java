package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.Scanner;

public class CLIView implements View {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showMessage(String text) {
        System.out.println(ANSI_RED+"ATTENTION!");
        System.out.println(text);
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
        System.out.println(ANSI_RESET+"Insert your username:");
        System.out.print(ANSI_CYAN+">");
        return input.nextLine();
    }

    public int getNumber(){
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_RESET+"Insert the number of players for your game:");
        System.out.print(ANSI_CYAN+">");
        return input.nextInt();
    }

    @Override
    public void startGame() {
        System.out.println(ANSI_GREEN+"  __  __                 _                                    __     ____                           _                                           \n" +
                " |  \\/  |   __ _   ___  | |_    ___   _ __   ___      ___    / _|   |  _ \\    ___   _ __     __ _  (_)  ___   ___    __ _   _ __     ___    ___ \n" +
                " | |\\/| |  / _` | / __| | __|  / _ \\ | '__| / __|    / _ \\  | |_    | |_) |  / _ \\ | '_ \\   / _` | | | / __| / __|  / _` | | '_ \\   / __|  / _ \\\n" +
                " | |  | | | (_| | \\__ \\ | |_  |  __/ | |    \\__ \\   | (_) | |  _|   |  _ <  |  __/ | | | | | (_| | | | \\__ \\ \\__ \\ | (_| | | | | | | (__  |  __/\n" +
                " |_|  |_|  \\__,_| |___/  \\__|  \\___| |_|    |___/    \\___/  |_|     |_| \\_\\  \\___| |_| |_|  \\__,_| |_| |___/ |___/  \\__,_| |_| |_|  \\___|  \\___|\n" +
                "                                                                                                                                                ");
        System.out.println("Game started");
    }


}
