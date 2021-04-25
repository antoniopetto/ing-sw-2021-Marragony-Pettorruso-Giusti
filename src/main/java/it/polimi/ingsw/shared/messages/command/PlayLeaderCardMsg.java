package it.polimi.ingsw.shared.messages.command;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.shared.messages.server.ErrorMsg;

import java.io.IOException;

public class PlayLeaderCardMsg implements CommandMsg {

    private int cardId;

    public PlayLeaderCardMsg(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public void execute(Game game, ClientHandler handler) throws IOException {
        boolean isPlayable = true;
        try{
            isPlayable = game.getPlaying().playLeaderCard(cardId);
        }catch (IllegalArgumentException e){
            ErrorMsg msg = new ErrorMsg(e.getMessage());
            handler.writeObject(msg);
        }catch (IllegalStateException i) {
            ErrorMsg msg = new ErrorMsg(i.getMessage());
            handler.writeObject(msg);
        }

        if(!isPlayable){
                ErrorMsg msg = new ErrorMsg("The player does not meet the requirements");
                handler.writeObject(msg);
            }
    }
}
