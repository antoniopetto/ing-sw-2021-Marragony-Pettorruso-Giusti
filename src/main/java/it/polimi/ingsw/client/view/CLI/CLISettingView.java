package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLISettingView{

    private CLIView cliView;

    public CLISettingView(CLIView cliView) {
        this.cliView = cliView;
    }

    public void execute() {

        String ip;
        int port = 7777;
        Socket server;
        ServerHandler serverHandler;
        boolean reachable = false;
        while(!reachable) {
            Scanner input = new Scanner(System.in);
            System.out.println("Server IP address?");
            System.out.print(">");
            ip = input.nextLine();

            System.out.println("Default port is 7777. Do you want to change it? [y/N]");
            System.out.print(">");
            while(true) {
                String choice = input.nextLine();
                if (choice.equals("") || choice.equalsIgnoreCase("n"))
                    break;
                else if (choice.equalsIgnoreCase("y")) {
                    System.out.println("Insert port number: ");
                    System.out.print(">");
                    port = input.nextInt();
                    break;
                } else
                    System.out.println("Invalid input");
            }
            try{
                server = new Socket(ip, port);
                serverHandler = new ServerHandler(server, cliView, cliView.getGame());
                System.out.println("You are connected to the server!");
                new Thread(serverHandler).start();
                reachable=true;
            } catch(IOException e) {
                System.out.println(Graphics.ANSI_RED+"Server unreachable, try again."+Graphics.ANSI_RESET);
            }
        }
    }
}
