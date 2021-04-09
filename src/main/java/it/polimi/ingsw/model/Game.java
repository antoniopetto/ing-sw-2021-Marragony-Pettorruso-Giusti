package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Game
{
    private boolean endgame;
    private final Optional<SoloRival> soloRival;
    private final boolean singlePlayer;
    private final List<Player> players = new ArrayList<>();

    public static Game newSinglePlayerGame(String username){
        return new Game(username);
    }

    public static Game newRegularGame(List<String> usernames){
        return new Game(usernames);
    }

    private Game(String username){
        singlePlayer = true;
        players.add(new Player(username));
        soloRival = Optional.of(new SoloRival());
    }

    private Game(List<String> usernames){
        if (usernames.size() > 4 || usernames.size() < 2)
            throw new IllegalArgumentException("Number of players out of bounds");

        singlePlayer = false;
        soloRival = Optional.empty();

        for (String s : usernames){
            players.add(new Player(s));
        }
        Collections.shuffle(players);
    }

    private Player findPlayer(String username){
        return players.stream()
                      .filter(player -> username.equals(player.getUsername()))
                      .findFirst().orElse(null);
    }

    public boolean isSinglePlayer() { return singlePlayer; }

}
