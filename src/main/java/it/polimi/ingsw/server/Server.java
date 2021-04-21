package it.polimi.ingsw.server;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final PublicWaitRoom publicRoom = PublicWaitRoom.getInstance();
    private static final int SOCKET_PORT = 7777;

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
                Socket client = socket.accept();
                Matchmaker matchmaker = new Matchmaker(client, publicRoom);
            }
            catch (IOException e){
                System.out.println("Error during connection");
            }
        }
    }
}
