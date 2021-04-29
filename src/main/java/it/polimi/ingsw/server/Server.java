package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    public static final PublicWaitRoom[] publicRooms = new PublicWaitRoom[4];
    private static final int SOCKET_PORT = 7777;
    public static final Set<String> activeUsernames = new HashSet<>();

    public static void main(String[] args){

        publicRooms[0] = new PublicWaitRoom(1);
        publicRooms[1] = new PublicWaitRoom(2);
        publicRooms[2] = new PublicWaitRoom(3);
        publicRooms[3] = new PublicWaitRoom(4);
        ServerSocket socket;
        try{
            socket = new ServerSocket(SOCKET_PORT);
        }
        catch (IOException e){
            System.out.println("Cannot open server socket");
            System.exit(1);
            return;
        }
        System.out.println("Server ready to accept connections!");
        while (true){

            try{
                Socket clientSocket = socket.accept();
                System.out.println(">>>>New connection with client [" + clientSocket.getInetAddress()+"]");
                new Thread(new Matchmaker(clientSocket)).start();
            }
            catch (IOException e){
                System.out.println("Connection dropped during thread creation");
            }

        }
    }

    public static void logOut(String username){
        if (username != null){
            activeUsernames.remove(username);
        }
    }
}
