package it.polimi.ingsw.shared.messages;

public class GameModeMsg {

    public enum GameMode{
        SINGLEPLAYER,
        MULTIPLAYER
    }

    private GameMode mode;

    GameModeMsg(GameMode mode){
        if (mode == null)
            throw new IllegalArgumentException("GameMode cannot be null");
        this.mode = mode;
    }

    public GameMode getMode() { return mode; }
}
