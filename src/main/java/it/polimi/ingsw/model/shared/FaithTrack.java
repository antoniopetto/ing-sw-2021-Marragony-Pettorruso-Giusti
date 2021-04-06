package it.polimi.ingsw.model.shared;

import it.polimi.ingsw.model.Player;

import java.util.*;

public class FaithTrack {

    private static final int LAST_POSITION = 20;
    private final List<Position> track = new ArrayList<>();
    private final List<Player> players;

    FaithTrack(List<Player> players, Map<Integer, Integer> victoryPointMap, List<Integer> sectionStarts, List<Integer> sectionEnds){

        if(sectionStarts.size() != 3 || sectionEnds.size() != 3 ||
                sectionStarts.get(0) < 1 || sectionEnds.get(2) > LAST_POSITION)
            throw new IllegalArgumentException();

        for(int i = 0; i < 3; i++){
            if (sectionStarts.get(i) > sectionEnds.get(i) ||
                    i > 0 && sectionStarts.get(i) <= sectionEnds.get(i-1))
                throw new IllegalArgumentException();
        }

        this.players = players;
        int nextReportSection = 0;
        boolean isLastSectionClosed = true;
        track.add(new Position(0, 0, false));

        for(int i = 1; i <= LAST_POSITION; i++){

            int currentVictoryPoints = victoryPointMap.containsKey(i) ?
                    victoryPointMap.get(i) : track.get(i-1).getVictoryPoints();

            if (sectionStarts.contains(i) && sectionEnds.contains(i)){
                track.add(new Position(i, currentVictoryPoints, true, OptionalInt.of(nextReportSection)));
                isLastSectionClosed = true;
                nextReportSection++;
            }
            else if (sectionStarts.contains(i)) {
                track.add(new Position(i, currentVictoryPoints, false, OptionalInt.of(nextReportSection)));
                isLastSectionClosed = false;
                nextReportSection++;
            }
            else if(sectionEnds.contains(i)){
                track.add(new Position(i, currentVictoryPoints, true, track.get(i-1).sectionNumber()));
                isLastSectionClosed = true;
            }
            else if(isLastSectionClosed){
                track.add(new Position(i, currentVictoryPoints, false));
            }
            else {
                track.add(new Position(i, currentVictoryPoints, true, track.get(i-1).sectionNumber()));
            }
        }

        for (Player p : players){
            p.setPosition(track.get(0));
        }

    }

    public void advance(Player player){

        int currentIndex = track.indexOf(player.getPosition());
        if(track.get(currentIndex).getNumber() == LAST_POSITION)
            throw new IllegalArgumentException("Player is already at the end of the track");

        player.setPosition(track.get(currentIndex + 1));

    }

    public void advanceAllBut(Player excludedPlayer){

        for(Player p : players){
            if(p != excludedPlayer)
                advance(p);
        }
    }
}