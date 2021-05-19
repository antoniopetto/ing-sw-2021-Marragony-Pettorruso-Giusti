package it.polimi.ingsw.server.model.shared;

import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.AbstractPlayer;
import it.polimi.ingsw.server.model.Game;

import java.util.*;

public class FaithTrack {

    private static final int LAST_POSITION = 24;
    private final List<Position> track = new ArrayList<>();
    private final List<AbstractPlayer> players = new ArrayList<>();
    private final Game game;
    private int nextVaticanReport = 0;
    private final VirtualView virtualView;

    /**
     * <code>FaithTrack</code> constructor.
     *
     * @param game          Game reference
     * @param virtualView   Virtual view reference, used to send updates
     * @param players       List of player to place in the track
     */
    public FaithTrack(Game game, VirtualView virtualView, List<? extends AbstractPlayer> players){

        this.game = game;
        this.virtualView = virtualView;
        this.players.addAll(players);

        Map<Integer, Integer> vPMap = Map.of(3, 1,
                                             6, 2,
                                             9, 4,
                                            12, 6,
                                            15, 9,
                                            18,12,
                                            21,16,
                                            24,20);

        SortedSet<Integer> sectionRanges = new TreeSet<>(List.of(5, 8, 12, 16, 19, 24));

        track.add(new Position(0, 0, false, -1));

        for(int i = 1; i <= LAST_POSITION; i++){

            int currentVP = vPMap.containsKey(i) ? vPMap.get(i) : track.get(i-1).getVictoryPoints();
            int currentSection;
            boolean isPopeSpace = false;

            int headSetSize = sectionRanges.headSet(i).size();

            if (sectionRanges.contains(i) && headSetSize % 2 == 1)
                isPopeSpace = true;
            if (sectionRanges.contains(i) || headSetSize % 2 == 1)
                currentSection = (headSetSize / 2);
            else currentSection = - 1;

            track.add(new Position(i, currentVP, isPopeSpace, currentSection));
        }

        for (AbstractPlayer p : players){
            p.setPosition(track.get(0));
        }
    }

    /**
     * Activates a vatican report.
     * Checks what's the next vatican report due in the <code>FaithTrack</code> and activates the tiles of the player
     * inside the correct vatican report section.
     */
    public void vaticanReport(){
        for (AbstractPlayer p : players){
            if (p.getPosition().sectionNumber() == nextVaticanReport)
                p.vaticanReportEffect(nextVaticanReport);
        }
        nextVaticanReport ++;
        virtualView.vaticanReportUpdate();
    }

    /**
     * Checks is a player is strictly first.
     *
     * @param player        The queried <code>Player</code>
     * @return              True if <code>player</code> is strictly first, false otherwise
     */
    public boolean isAbsoluteFirst(AbstractPlayer player){

        for(AbstractPlayer p : players)
            if(!p.equals(player) && player.getPosition().getNumber() <= p.getPosition().getNumber())
                return false;
        return true;
    }

    /**
     * Makes the requested player advance of one position in the <code>FaithTrack</code>.
     * Triggers <code>Game.setLastRoung()</code> if the player has reached the end of the track
     *
     * @param player        The player that is advancing
     */
    public void advance(AbstractPlayer player){

        Position currentPos = player.getPosition();
        int currentIndex = track.indexOf(currentPos);

        if(currentPos.getNumber() < LAST_POSITION) {
            player.setPosition(track.get(currentIndex + 1));
            if (player.getPosition().isPopeSpace() && isAbsoluteFirst(player))
                vaticanReport();
            if (player.getPosition().getNumber() == LAST_POSITION)
                game.setLastRound();
        }
        virtualView.faithTrackUpdate(player, false);
    }

    /**
     * Makes all the player but one advance of one position in the <code>FaithTrack</code>.
     * Triggers <code>Game.setLastRoung()</code> if one player has reached the end of the track
     *
     * @param excludedPlayer        The player that is not advancing
     */
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
                    game.setLastRound();
            }
        }

        if(isVaticanReportDue)
            vaticanReport();

        virtualView.faithTrackUpdate(excludedPlayer, true);
    }

    public List<Position> getTrack(){
        return new ArrayList<>(track);
    }
}