package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.GameController;

public class GoBackMsg implements CommandMsg{

    @Override
    public void execute(GameController gameController){
        gameController.goBack();
    }

}
