package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.Resource;

import java.io.Serializable;
import java.util.*;

public class SimplePlayer implements Serializable {
    private final String username;
    private int position;
    private SimpleWarehouse warehouse;
    private final ArrayList<SimpleSlot> slots;
    private Map<Resource, Integer> strongbox = new HashMap<>();
    private final List<SimpleLeaderCard> leaderCards = new ArrayList<>();
    private final List<ProductionPower> extraProductionPowers = List.of(new ProductionPower(2, 1));
    private final Set<Resource> whiteMarbleAliases = new HashSet<>();

    public SimplePlayer(String username) {
        this.username = username;
        this.slots = new ArrayList<>();
        this.position = 0;
        this.warehouse = new SimpleWarehouse();

        slots.add(new SimpleSlot(1));
        slots.add(new SimpleSlot(2));
        slots.add(new SimpleSlot(3));
    }

    public void addLeaderCards(Collection<Integer> cardIds){
        cardIds.forEach(cardId -> leaderCards.add(SimpleLeaderCard.parse(cardId)));
    }

    public Map<Resource, Integer> getStrongbox() {
        return strongbox;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void changeWarehouse(SimpleWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void activeLeaderCard(int cardId){
        for (SimpleLeaderCard card : leaderCards)
            if (card.getId() == cardId) card.setActive(true);
    }

    public void insertCardInSlot(int cardId, int slotIdx){
        slots.get(slotIdx).addCard(cardId);
    }

    public void discardLeaderCard(int cardId){
        leaderCards.removeIf(card -> card.getId() == cardId);
    }

    public List<SimpleLeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public int chooseLeaderCard(int position){
        return leaderCards.get(position-1).getId();
    }

    public SimpleWarehouse getWarehouse() {
        return warehouse;
    }

    public int getPosition() {
        return position;
    }

    public void changeStrongbox(Map<Resource, Integer> strongbox) {
        this.strongbox=strongbox;
    }

    public List<SimpleSlot> getSlots() {
        return slots;
    }

    public void setWhiteMarbleAliases(Set<Resource> aliases){
        whiteMarbleAliases.clear();
        whiteMarbleAliases.addAll(aliases);
    }

    public Set<Resource> getWhiteMarbleAliases(){
        return whiteMarbleAliases;
    }

    public void addExtraProductionPower(ProductionPower power){
        extraProductionPowers.add(power);
    }

    public List<ProductionPower> getExtraProductionPowers(){
        return extraProductionPowers;
    }
}
