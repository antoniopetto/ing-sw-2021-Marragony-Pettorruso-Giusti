package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLISettingView{

    private View cliView;

    public CLISettingView(View cliView) {
        this.cliView = cliView;
    }

    public void execute() {
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
