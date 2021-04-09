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

    public DevelopmentCard(int victoryPoints, int level, CardColor color, ResourceRequirement requirement, ProductionPower power)
    {
        super(victoryPoints);
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

    /**
     *this method confront the levels of two cards
     * @param card is the other card to confront
     * @return true if the level of this is higher than the level of card
     */
    public boolean isLevelHigher(DevelopmentCard card)
    {
        return this.level> card.getLevel();
    }
}
