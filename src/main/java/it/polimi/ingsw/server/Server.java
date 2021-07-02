package it.polimi.ingsw.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Server {

    /**
     * Static block needed to set a System property with the location of the base folder where we can save data from
     * the execution. In the server it's needed by the logger and the save file operations.
     */
    static {
        try{
            URI jarUri = VirtualView.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            URI baseDirUri = jarUri.getPath().endsWith("/") ? jarUri.resolve("..") : jarUri.resolve(".");
            System.setProperty("mor.base", baseDirUri.getPath());
        }
        catch (URISyntaxException e){
            System.out.println("Could not get baseDir");
        }
    }

    public static final Logger logger = LogManager.getLogger(Server.class);
    public static final PublicWaitRoom[] publicRooms = new PublicWaitRoom[4];
    private static int SOCKET_PORT = 7777;
    public static final Set<String> activeUsernames = new HashSet<>();

    /**
     * After creating the public wait rooms, the server asks for the port number to open the socket.
     * Then for each request, creates a ClientHandler and starts a Matchmaker in a new Thread.
     * @param args      Ignored arguments
     */
    public static void main(String[] args){

        logger.debug(System.lineSeparator());
        logger.info("Starting server");
        publicRooms[0] = new PublicWaitRoom(1);
        publicRooms[1] = new PublicWaitRoom(2);
        publicRooms[2] = new PublicWaitRoom(3);
        publicRooms[3] = new PublicWaitRoom(4);
        ServerSocket socket;
        System.out.println("Default port is 7777. Do you want to change it? [y/N]");
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        while(true){
            String choice = input.nextLine();
            if(choice.equals("") || choice.equalsIgnoreCase("n"))
                break;
            else if(choice.equalsIgnoreCase("y")) {
                System.out.println("Insert port number: ");
                System.out.print(">");
                SOCKET_PORT = input.nextInt();
                break;
            }
            else
                System.out.println("Invalid input");
        }
        try{
            socket = new ServerSocket(SOCKET_PORT);
        }
        catch (IOException e){
            logger.fatal("Cannot open server socket. Terminating");
            System.exit(1);
            return;
        }
        logger.info("Server ready to accept connections!");
        while (true){
            try{
                Socket clientSocket = socket.accept();
                logger.info("New connection with client [" + clientSocket.getInetAddress() + "]");
                new Thread(new Matchmaker(clientSocket)).start();
            }
            catch (IOException e){
                logger.warn("Connection dropped");
            }
        }
    }

    /**
     * Removes a username from the set of those connected to the server
     * @param username  The name of the player disconnecting
     */
    public static void logOut(String username){
        if (username != null){
            Server.logger.info("Logging out [" + username + "]");
            activeUsernames.remove(username);
        }
    }

    /**
     * Creates an unique string for every different collection of usernames, using "&" as separator
     * @param usernames     The collection of usernames
     * @return              The codified game name
     */
    public static String formatGameName(Collection<String> usernames){
        List<String> usernameList = new ArrayList<>(usernames);
        Collections.sort(usernameList);
        StringBuilder sb = new StringBuilder();
        sb.append(usernameList.get(0));
        for (int i = 1; i < usernameList.size(); i++)
            sb.append("&").append(usernameList.get(i));
        return sb.toString();
    }
}
