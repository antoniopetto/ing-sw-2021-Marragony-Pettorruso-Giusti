package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.messages.view.*;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class Matchmaker implements Runnable{

    private static final String USERNAME_PATTERN = "[a-zA-Z0-9._-]{3,20}";
    private final ClientHandler handler;
    private String username;

    Matchmaker(Socket clientSocket) throws IOException{
        handler = new ClientHandler(clientSocket);
    }

    public void run(){
        try{
            while(true) {
                handler.writeObject(new UsernameRequestMsg());
                String username = ((UsernameMsg) handler.readObject()).getUsername();
                if (Server.activeUsernames.contains(username))
                    handler.writeObject(new ErrorMsg("This username is already in use. Try a new one"));
                else if (!Pattern.matches(USERNAME_PATTERN, username))
                    handler.writeObject(new ErrorMsg("Illegal characters: username must be a 3-20 characters long string" +
                            "containing alphanumeric or special [._-] characters"));
                else {
                    this.username = username;
                    Server.activeUsernames.add(this.username);
                    break;
                }
            }

            handler.writeObject(new NPlayerRequestMsg());
            int nPlayers = ((NPlayerMsg) handler.readObject()).getNPlayers();
            if (nPlayers < 1 || nPlayers > 4){
                handler.writeObject(new ErrorMsg("Illegal number of players"));
                terminate();
                return;
            }
            else {
                Server.publicRooms[nPlayers - 1].add(username, handler);
            }
        }
        catch (IOException e){
            System.out.println("Connection dropped");
            terminate();
        }
        catch (ClassNotFoundException e){
            System.out.println("Illegal object sent to matchmaker");
            terminate();
        }
    }

    private void terminate(){
        handler.closeConnection();
        Server.logOut(username);
    }

}
