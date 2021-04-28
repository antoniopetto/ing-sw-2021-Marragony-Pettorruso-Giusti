package it.polimi.ingsw.client.views.CLI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.views.SettingView;
import it.polimi.ingsw.client.views.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLISettingView implements SettingView {

    private View cliView = new CLIView();

    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        String ip;
        int port;
        Socket server;
        ServerHandler serverHandler;
        boolean reachable = false;
        while(!reachable)
        {
            System.out.println("Server IP address?");
            System.out.print(">");
            ip = input.nextLine();
            System.out.println("Server port?");
            System.out.print(">");
            port = input.nextInt();
            try{
                server = new Socket(ip, port);
                serverHandler = new ServerHandler(server, cliView);
                System.out.println("You are connected to the server!");
                new Thread(serverHandler).start();
                reachable=true;
            }catch(IOException e)
            {
                System.out.println("Server unreachable, try again.");
            }
        }


    }
}
