package it.polimi.ingsw.messages.toview;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.View;

import java.util.Map;

public class EndGameMsg implements ViewMsg{

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
        if (win != null)
            view.showTextMessage(win ? "You won!" : "You lost.");
        view.showLeaderboard(leaderboard);
        view.endGame();
    }

    @Override
    public String toString() {
        return "LeaderboardMsg{}";
    }
}
