package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Server {

    public static final PublicWaitRoom[] publicRooms = new PublicWaitRoom[4];
    private static int SOCKET_PORT = 7777;
    public static final Set<String> activeUsernames = new HashSet<>();

    public static void main(String[] args){

        publicRooms[0] = new PublicWaitRoom(1);
        publicRooms[1] = new PublicWaitRoom(2);
        publicRooms[2] = new PublicWaitRoom(3);
        publicRooms[3] = new PublicWaitRoom(4);
        ServerSocket socket;
        System.out.println("Default port is 7777. Want to change it?");
        System.out.println("1) Yes");
        System.out.println("2) No");
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        int choice;
        boolean valid = false;
        while(!valid)
        {
            try {
                choice = input.nextInt();
                if(choice<1||choice>2) throw new InputMismatchException();
                if(choice == 1)
                {
                    System.out.println("Insert port number: ");
                    System.out.print(">");
                    SOCKET_PORT = input.nextInt();
                }
                valid=true;
            }catch (Exception e)
            {
                System.out.println("Invalid input");
            }
        }
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
