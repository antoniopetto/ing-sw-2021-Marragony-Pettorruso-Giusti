package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.ProductionPower;
import it.polimi.ingsw.model.playerboard.*;
import it.polimi.ingsw.model.shared.Position;

import java.util.*;

public class Player {

    private final String username;
    private Position position;
    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard();
    private final Set<Resource> activeDiscount = new HashSet<>();
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();

    Player(String username){
        this.username = username;
        tiles.add(new PopeFavourTile(2));
        tiles.add(new PopeFavourTile(3));
        tiles.add(new PopeFavourTile(4));
    }

    public String getUsername() { return username; }

    public int countPoints(){
        int counter = 0;
        for (PopeFavourTile t : tiles){
            counter += t.isGained() ? t.getValue() : 0;
        }
        counter += playerBoard.countVictoryPoints();
        counter += position.getVictoryPoints();
        // add leaderCardPoints
        return counter;
    }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Set<Resource> getActiveDiscount() {
        return activeDiscount;
    }

    public void addActiveDiscount(Resource resource) {
        activeDiscount.add(resource);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    public void addWhiteMarbleAlias(Resource resource) {
        whiteMarbleAliases.add(resource);
    }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

}