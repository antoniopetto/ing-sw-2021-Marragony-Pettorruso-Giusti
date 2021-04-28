package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;

import java.util.*;

public class FaithTrack {

    private static final int LAST_POSITION = 24;
    private final List<Position> track = new ArrayList<>();
    private final List<AbstractPlayer> players;
    private final Game game;
    private int vaticanReportCounter = 0;
    private final VirtualView virtualView;

    public FaithTrack(Game game, VirtualView virtualView, List<AbstractPlayer> players){

        this.game = game;
        this.virtualView = virtualView;
        this.players = players;

        Map<Integer, Integer> vPMap = Map.of(3, 1,
                                             6, 2,
                                             9, 4,
                                            12, 6,
                                            15, 9,
                                            18,12,
                                            21,16,
                                            24,20);

        SortedSet<Integer> sectionRanges = new TreeSet<>(List.of(5, 8, 12, 16, 19, 24));

        track.add(new Position(0, 0, false, 0));

        for(int i = 1; i <= LAST_POSITION; i++){

            int currentVP = vPMap.containsKey(i) ? vPMap.get(i) : track.get(i-1).getVictoryPoints();
            int currentSection;
            boolean isPopeSpace = false;

            int headSetSize = sectionRanges.headSet(i).size();

            if (sectionRanges.contains(i) && headSetSize % 2 == 1)
                isPopeSpace = true;
            if (sectionRanges.contains(i) || headSetSize % 2 == 1)
                currentSection = (headSetSize / 2) + 1;
            else currentSection = 0;

            track.add(new Position(i, currentVP, isPopeSpace, currentSection));
        }

        for (AbstractPlayer p : players){
            p.setPosition(track.get(0));
        }

    }

    public void vaticanReport(){
        vaticanReportCounter++;
        for (AbstractPlayer p : players){
            if (p.getPosition().sectionNumber() == vaticanReportCounter)
                p.vaticanReportEffect(vaticanReportCounter + 1);
        }
        virtualView.vaticanReportUpdate();
    }

    public boolean isAbsoluteFirst(AbstractPlayer player){

        for(AbstractPlayer p : players)
            if(!p.equals(player) && player.getPosition().getNumber() <= p.getPosition().getNumber())
                return false;
        return true;
    }

    public void advance(AbstractPlayer player){

        Position currentPos = player.getPosition();
        int currentIndex = track.indexOf(currentPos);

        if(currentPos.getNumber() < LAST_POSITION) {
            player.setPosition(track.get(currentIndex + 1));
            if (player.getPosition().isPopeSpace() && isAbsoluteFirst(player))
                vaticanReport();
            if (player.getPosition().getNumber() == LAST_POSITION)
                game.setLastRound(true);
        }

        virtualView.faithTrackUpdate(player, false);
    }

    public void advanceAllBut(AbstractPlayer excludedPlayer){

        boolean isVaticanReportDue = false;

        for(AbstractPlayer p : players){
            Position currentPos = p.getPosition();
            int currentIndex = track.indexOf(currentPos);

            if(!p.equals(excludedPlayer) && currentPos.getNumber() < LAST_POSITION) {
                p.setPosition(track.get(currentIndex + 1));
                if (p.getPosition().isPopeSpace() && isAbsoluteFirst(p))
                    isVaticanReportDue = true;
                if (p.getPosition().getNumber() == LAST_POSITION)
                    game.setLastRound(true);
            }
        }

        if(isVaticanReportDue)
            vaticanReport();

        virtualView.faithTrackUpdate(excludedPlayer, true);
    }
}