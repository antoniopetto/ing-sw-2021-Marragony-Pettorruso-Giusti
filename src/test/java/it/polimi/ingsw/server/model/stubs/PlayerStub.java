package it.polimi.ingsw.server.model.stubs;

import it.polimi.ingsw.server.model.Player;

public class PlayerStub extends Player {
    private PlayerBoardStub playerBoard;


    public PlayerStub(String username) {
        super(username);
    }

    @Override
    public PlayerBoardStub getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(PlayerBoardStub playerBoard) {
        this.playerBoard = playerBoard;
    }
}
