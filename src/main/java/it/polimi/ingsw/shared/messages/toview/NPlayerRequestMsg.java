package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.command.NPlayerMsg;

/**
 * This class is the message sent to ask the number of players for the game.
 */
public class NPlayerRequestMsg implements ToViewMsg {

    @Override
    public void changeView(View view, ServerHandler handler) {
        boolean valid=false;
        while(!valid) {
            try {
                int number = view.getNumberOfPlayers();
                NPlayerMsg msg = new NPlayerMsg(number);
                handler.writeObject(msg);
                valid=true;
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Try again");
            }
        }
    }

    @Override
    public String toString() {
        return "NPlayerRequestMsg{}";
    }
}
