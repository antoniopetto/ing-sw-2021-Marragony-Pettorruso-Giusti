package it.polimi.ingsw.model;

import it.polimi.ingsw.model.playerboard.*;
import it.polimi.ingsw.model.shared.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    private final String username;
    private Position position;
    private final List<PopeFavourTile> tiles = new ArrayList<>();
    private final PlayerBoard playerBoard = new PlayerBoard(new StrongBox(), new WareHouse(), new ArrayList<>());
    private final Set<Resource> activeDiscount = new HashSet<>();
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();
    private final Set<Resource> extraProductionPower = new HashSet<>();

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
        return counter;
    }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }

    public Set<Resource> getActiveDiscount() {
        return activeDiscount;
    }

    public void setActiveDiscount(Resource resource) {
        activeDiscount.add(resource);
    }

    public Set<Resource> getWhiteMarbleAliases() {
        return whiteMarbleAliases;
    }

    public void setWhiteMarbleAliases(Resource resource) {
        whiteMarbleAliases.add(resource);
    }

    public void setExtraProductionPower(Resource res) {
        extraProductionPower.add(res);
    }

    public Position getPosition() { return position; }

    public void setPosition(Position position) { this.position = position; }

}
