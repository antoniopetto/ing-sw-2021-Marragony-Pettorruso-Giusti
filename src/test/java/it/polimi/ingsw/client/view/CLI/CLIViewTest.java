package it.polimi.ingsw.client.view.CLI;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CLIViewTest {

    @Test
    public void endGameTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("aaa", 20);
        map.put("bbb", 30);
        map.put("ccc", 15);
        map.put("ddd", 40);
        CLIView view = new CLIView();
        view.showLeaderboard(map);
    }

}