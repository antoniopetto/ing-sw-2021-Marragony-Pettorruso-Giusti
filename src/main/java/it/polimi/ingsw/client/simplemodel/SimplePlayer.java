package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.PopeFavourTile;

import java.io.Serializable;
import java.util.*;

public class SimplePlayer implements Serializable {
    private final String username;
    private int position;
    private SimpleWarehouse warehouse;
    private ArrayList<SimpleSlot> slots;
    private Map<Resource, Integer> strongbox = new HashMap<>();
    private List<SimpleLeaderCard> leaderCards = new ArrayList<>();
    private List<ProductionPower> extraProductionPowers = new ArrayList<>();
    private Set<Resource> whiteMarbleAliases = new HashSet<>();
    private Set<Resource> activeDiscounts = new HashSet<>();
    private List<PopeFavourTile> tiles = new ArrayList<>();

    public SimplePlayer(String username) {
        this.username = username;
        this.slots = new ArrayList<>();
        this.position = 0;
        this.warehouse = new SimpleWarehouse();
        extraProductionPowers.add(new ProductionPower(2, 1));

        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
        slots.add(new SimpleSlot());
    }

    public void setLeaderCards(List<SimpleLeaderCard> cards){ leaderCards = new ArrayList<>(cards);}

    public void setLeaderCards(Set<Integer> cardIds){
        leaderCards.clear();
        cardIds.forEach(i -> leaderCards.add(SimpleLeaderCard.parse(i)));
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

    public void activateLeaderCard(int cardId){

        Optional<SimpleLeaderCard> card = leaderCards.stream().filter(i -> i.getId() == cardId).findAny();
        if (card.isPresent())
            card.get().setActive(true);
        else {
            SimpleLeaderCard newCard = SimpleLeaderCard.parse(cardId);
            newCard.setActive(true);
            leaderCards.add(newCard);
        }
    }

    public void addCardDiscount(Resource resource){
        activeDiscounts.add(resource);
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
        whiteMarbleAliases = new HashSet<>(aliases);
    }

    public void setTiles(List<PopeFavourTile> tiles){
        this.tiles = new ArrayList<>(tiles);
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

    public void setWarehouse(SimpleWarehouse warehouse){
        this.warehouse = warehouse;
    }

    public void setSlots(List<SimpleSlot> slots){
        this.slots = new ArrayList<>(slots);
    }

    public void setStrongbox(Map<Resource, Integer> strongbox){
        this.strongbox = new HashMap<>(strongbox);
    }

    public void setExtraProductionPowers(List<ProductionPower> powers){
        extraProductionPowers = new ArrayList<>(powers);
    }

    public void setActiveDiscounts(Set<Resource> discounts){
        activeDiscounts = new HashSet<>(discounts);
    }

    public Set<Resource> getActiveDiscounts(){
        return activeDiscounts;
    }

    public void clearLeaderCards(){
        leaderCards.clear();
    }

    public void gainTile(int tileIdx){

        tiles.get(tileIdx).gain();
    }

    public List<PopeFavourTile> getTiles(){
        return tiles;
    }
}
