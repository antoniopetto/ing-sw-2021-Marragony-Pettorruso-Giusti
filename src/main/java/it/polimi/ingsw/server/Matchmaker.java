package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.messages.command.NPlayerMsg;
import it.polimi.ingsw.shared.messages.command.UsernameMsg;
import it.polimi.ingsw.shared.messages.toview.*;

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

    /**
     * The Matchmaker asks the client preliminary questions, such as the username (checking for a valid string)
     * and the number of players of the game he wants to get in. After getting these info, the player is placed in a
     * waiting room of the appropriate size.
     */
    public void run(){
        try{
            while(true) {

                handler.writeObject(new UsernameRequestMsg());
                String username = ((UsernameMsg) handler.readObject()).getUsername();

                if (Server.activeUsernames.contains(username)){
                    handler.writeObject(new ErrorMsg("This username is already in use. Try a new one"));
                }
                else if (!Pattern.matches(USERNAME_PATTERN, username))
                    handler.writeObject(new ErrorMsg("Illegal characters: username must be a 3-20 characters long string" +
                            "containing alphanumeric or special [._-] characters"));
                else {
                    this.username = username;
                    Server.logger.info("Client [" + handler.getIP() + "] chose username " + username);
                    Server.activeUsernames.add(this.username);
                    break;
                }
            }

            while (true) {
                handler.writeObject(new NPlayerRequestMsg());
                int nPlayers = ((NPlayerMsg) handler.readObject()).getNPlayers();
                if (nPlayers < 1 || nPlayers > 4) {
                    handler.writeObject(new ErrorMsg("Illegal number of players"));
                }
                else {
                    Server.publicRooms[nPlayers - 1].add(username, handler);
                    break;
                }
            }
        }
        catch (IOException e){
            Server.logger.warn("Connection dropped with client [" + handler.getIP() + "]");
            terminate();
        }
        catch (ClassNotFoundException e){
            Server.logger.warn("Unrecognized object received by client [" + handler.getIP() + "]");
            terminate();
        }
    }

    private void terminate(){
        Server.logger.info("Disconnecting [" + handler.getIP() + "]");
        handler.closeConnection();
        Server.logOut(username);
    }
}
