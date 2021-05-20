package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLISettingView{

    private final CLIView cliView;

    public CLISettingView(CLIView cliView) {
        this.cliView = cliView;
    }

    public void execute() {

        boolean reachable = false;
        while(!reachable) {

            String ip = CLIView.askString("Insert server ip address:");
            boolean choice = CLIView.askYesNo("Default port is 7777. Do you want to change it?", false);
            int port = (choice) ? CLIView.askNumber("Insert port number", 0, 65535) : 7777;

            try{
                Socket server = new Socket(ip, port);
                ServerHandler serverHandler = new ServerHandler(server, cliView, cliView.getGame());
                System.out.println("You are connected to the server!");
                new Thread(serverHandler).start();
                reachable = true;
            } catch(IOException e) {
                System.out.println(Graphics.ANSI_RED+"Server unreachable, try again."+Graphics.ANSI_RESET);
            }
        }
    }
}
