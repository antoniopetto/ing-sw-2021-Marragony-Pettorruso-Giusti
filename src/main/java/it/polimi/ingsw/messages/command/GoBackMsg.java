package it.polimi.ingsw.messages.command;

import it.polimi.ingsw.server.model.Game;

public class GoBackMsg implements CommandMsg{
    public enum State{
        BEGIN_TURN,
        MANAGE_RESOURCES
    }

    private State state;

    public GoBackMsg(State state) {
        this.state = state;
    }

    @Override
    public void execute(Game game){
        game.goBack(this.state);
    }


}
