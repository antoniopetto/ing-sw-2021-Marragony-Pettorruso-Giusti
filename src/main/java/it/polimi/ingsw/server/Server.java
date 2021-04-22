package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    public static final PublicWaitRoom publicRoom = PublicWaitRoom.getInstance();
    private static final int SOCKET_PORT = 7777;
    public static final Set<String> activeUsernames = new HashSet<>();

    public static void main(String[] args){

        ServerSocket socket;
        try{
            socket = new ServerSocket(SOCKET_PORT);
        }
        catch (IOException e){
            System.out.println("Cannot open server socket");
            System.exit(1);
            return;
        }

        while (true){

            try{
                Socket clientSocket = socket.accept();
                new Thread(new Matchmaker(clientSocket)).start();
            }
            catch (IOException e){
                System.out.println("Connection dropped during thread creation");
            }
        }
    }

    public static void logOut(String username){
        if (username != null && activeUsernames.contains(username)){
            activeUsernames.remove(username);
        }
    }
}
