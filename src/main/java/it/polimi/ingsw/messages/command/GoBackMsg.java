package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;

public class GoBackMsg implements CommandMsg{

    @Override
    public void execute(Game game){
        game.goBack();
    }

}
