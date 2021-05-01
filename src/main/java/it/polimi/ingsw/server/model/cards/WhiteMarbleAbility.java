package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.util.Objects;

/**
 * This class represents the white marble power ability. The attribute resource represents the value of the white
 * marble when bought in the market.
 */
public class WhiteMarbleAbility implements SpecialAbility{
    private final Resource resource;
    public WhiteMarbleAbility(Resource res)
    {
        this.resource=res;
    }
    @Override
    public void activateAbility(Player player) {
        player.addWhiteMarbleAlias(this.resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhiteMarbleAbility that = (WhiteMarbleAbility) o;
        return resource == that.resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
}
