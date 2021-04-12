package it.polimi.ingsw.model.cards;

import java.util.List;

/**
 * This class represents a development card and extends the abstract class "Card". It has an integer attribute for the
 * level, a CardColor attribute for the color, a ResourceRequirement attribute for its requirements and a ProductionPower
 * attribute for its production power.
 */
public class DevelopmentCard extends Card{
    private final int level;
    private final CardColor color;
    private final List<ResourceRequirement> requirements;
    private final ProductionPower power;

    public DevelopmentCard(int id, int victoryPoints, int level, CardColor color, List<ResourceRequirement> requirements, ProductionPower power)
    {
        super(id, victoryPoints);
        this.level = level;
        this.color = color;
        this.requirements = requirements;
        this.power = power;
    }

    public int getLevel() {
        return level;
    }

    public CardColor getColor() {
        return color;
    }

    public ProductionPower getPower() {
        return power;
    }

    public List<ResourceRequirement> getRequirement() {
        return requirements;
    }

    /**
     *this method confront the levels of two cards
     * @param card is the other card to confront
     * @return true if the level of this is higher by 1 than the level of <code>card</code>
     */
    public boolean isLevelHigher(DevelopmentCard card)
    {
        return this.level== card.getLevel()+1;
    }
}
