package it.polimi.ingsw.shared.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.util.Map;

/**
 * This class is the message that notify the end of a game and the victory of a player.
 */
public class EndGameMsg implements ToViewMsg {

    private final Map<String, Integer> leaderboard;
    private Boolean win;

    public EndGameMsg(Map<String, Integer> leaderboard){
        this.leaderboard = leaderboard;
    }

    public EndGameMsg(Map<String, Integer> leaderboard, boolean win){
        this.leaderboard = leaderboard;
        this.win = win;
    }

    @Override
    public void changeView(View view, ServerHandler handler) {
        view.victory(win, leaderboard);
    }

    @Override
    public String toString() {
        return "EndGameMsg";
    }
}
