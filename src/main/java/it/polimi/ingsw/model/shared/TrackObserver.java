package it.polimi.ingsw.model.shared;

import it.polimi.ingsw.model.AbstractPlayer;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * This class implements an observer of the Faith Track using Observer pattern. After every change of position in the
 * Faith Track, it checks whether to activate the vatican report or the end of the game
 */
public class TrackObserver {
    private FaithTrack track;

    public TrackObserver(FaithTrack track)
    {
        this.track=track;
    }

    public void update(AbstractPlayer player)
    {
        if (player.getPosition().isPopeSpace() && track.isAbsoluteFirst(player))
            track.vaticanReport();

        if(player.getPosition().getNumber()==track.getLastPosition())
            player.activateEndGame();
    }

    public void update(List<AbstractPlayer> players)
    {
        for(AbstractPlayer player : players)
        {
            if (player.getPosition().isPopeSpace() && track.isAbsoluteFirst(player))
            {
                track.vaticanReport();
                return;
            }

            if(player.getPosition().getNumber()==track.getLastPosition())
            {
                player.activateEndGame();
                return;
            }
        }
    }
}
