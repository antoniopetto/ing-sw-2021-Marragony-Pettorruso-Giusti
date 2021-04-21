package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.AbstractPlayer;

import java.util.*;

public class FaithTrack {

    private static final int LAST_POSITION = 24;
    private static final int N_SECT = 3;
    private final List<Position> track = new ArrayList<>();
    private final List<AbstractPlayer> players;
    private int vaticanReportCounter = 0;
    private VirtualView observer;



    public FaithTrack(List<AbstractPlayer> players, Map<Integer, Integer> victoryPointsMap, int[][] sectionRanges){

        if (!legalSectionRanges(sectionRanges))
            throw new IllegalArgumentException("Illegal section ranges in configuration");

        this.players = players;

        track.add(new Position(0, 0, false, 0));

        for(int i = 1; i <= LAST_POSITION; i++){

            int currentVictoryPoints = victoryPointsMap.containsKey(i) ?
                    victoryPointsMap.get(i) : track.get(i-1).getVictoryPoints();

            boolean popeSpace = false;
            for(int j = 0; j < N_SECT; j++)
                popeSpace |= (sectionRanges[j][1] == i);

            track.add(new Position(i, currentVictoryPoints, popeSpace, posToSectionNumber(i, sectionRanges)));
        }

        for (AbstractPlayer p : players){
            p.setPosition(track.get(0));
        }



    }

    private boolean legalSectionRanges(int[][] ranges){
        if(ranges[0][0] < 1 || ranges[N_SECT][1] > LAST_POSITION)
            return false;

        for(int i = 0; i < N_SECT; i++)
            if (ranges[i][0] > ranges[i][1] || (i != 0 && ranges[i][0] < ranges[i-1][1]))
                return false;

        return true;
    }

    /**
     * Given the section ranges as formatted in the xml, finds the section number of a given position number.
     * If the position is not inside a range returns 0.
     *
     * @param pos       The effective position number in the track.
     * @param ranges    The array of ranges.
     * @return          The section number if inside one, 0 otherwise.
     */
    private int posToSectionNumber(int pos, int[][] ranges){

        for (int i = 0; i < N_SECT; i++){
            if (ranges[i][0] <= pos && pos <= ranges[i][1])
                return i + 1;
        }
        return 0;
    }

    public void vaticanReport(){
        vaticanReportCounter++;
        for (AbstractPlayer p : players){
            if (p.getPosition().sectionNumber() == vaticanReportCounter)
                p.vaticanReportEffect(vaticanReportCounter + 1);
        }
        observer.vaticanReportUpdate();
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
        }
        observer.faithTrackUpdate(player, false);
    }

    public void
    advanceAllBut(AbstractPlayer excludedPlayer){

        boolean isVaticanReportDue = false;
        List<AbstractPlayer> playerList = new ArrayList<>();
        for(AbstractPlayer p : players){
            Position currentPos = p.getPosition();
            int currentIndex = track.indexOf(currentPos);

            if(!p.equals(excludedPlayer) && currentPos.getNumber() < LAST_POSITION)
            {
                p.setPosition(track.get(currentIndex + 1));
                playerList.add(p);
            }

                if (p.getPosition().isPopeSpace() && isAbsoluteFirst(p))
                   isVaticanReportDue = true;
        }

        if(isVaticanReportDue)
            vaticanReport();
        observer.faithTrackUpdate(excludedPlayer, true);
    }

    public int getLastPosition()
    {
        return LAST_POSITION;
    }
}