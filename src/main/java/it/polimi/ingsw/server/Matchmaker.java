package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.messages.*;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class Matchmaker implements Runnable{

    private static final String USERNAME_PATTERN = "[a-zA-Z0-9[._-]]{3,20}";
    private ClientHandler handler;
    private String username;

    Matchmaker(Socket clientSocket) throws IOException{
        handler = new ClientHandler(clientSocket);
    }

    public void run(){
        try{
            while(true) {
                handler.writeObject(new UsernameRequest());
                String username = ((UsernameMsg) handler.readObject()).getUsername();
                if (Server.activeUsernames.contains(username))
                    handler.writeObject(new LogMsg("This username is already in use. Try a new one"));
                else if (Pattern.matches(USERNAME_PATTERN, username))
                    handler.writeObject(new LogMsg("Illegal characters: username must be a 3-20 characters long string" +
                            "containing alphanumeric or special [._-] characters"));
                else {
                    this.username = username;
                    Server.activeUsernames.add(this.username);
                    break;
                }
            }

            handler.writeObject(new GameModeRequest());
            GameModeMsg gameModeMsg = (GameModeMsg) handler.readObject();
            if(gameModeMsg.getMode().equals(GameModeMsg.GameMode.SINGLEPLAYER)) {
                new Thread(new VirtualView(username, handler)).start();
                return;
            }

            Server.publicRoom.add(username, handler);

        }
        catch (IOException e){
            System.out.println("Connection dropped");
            Server.logOut(username);
        }
        catch (ClassNotFoundException e){
            System.out.println("Illegal object sent to matchmaker");
            Server.logOut(username);
        }
    }

}
