package it.polimi.ingsw.model.cards;

/**
 * This class represents a development card and extends the abstract class "Card". It has an integer attribute for the
 * level, a CardColor attribute for the color, a ResourceRequirement attribute for its requirements and a ProductionPower
 * attribute for its production power.
 */
public class DevelopmentCard extends Card{
    private final int level;
    private final CardColor color;
    private final ResourceRequirement requirement;
    private final ProductionPower power;

    public DevelopmentCard(int id, int victoryPoints, int level, CardColor color, ResourceRequirement requirement, ProductionPower power)
    {
        super(id, victoryPoints);
        this.level = level;
        this.color = color;
        this.requirement = requirement;
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

    public ResourceRequirement getRequirement() {
        return requirement;
    }
}
