package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.*;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.cards.ProductionPower;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.server.model.shared.PopeFavourTile;
import org.apache.logging.log4j.ThreadContext;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CLIView implements View {

    private SimpleModel game;
    private ServerHandler serverHandler;
    private String ip;
    private int port;

    private final String column1Format = "%-2s";
    private final String column2Format = "%-2s";
    private final String column3Format = "%2s";
    private final String column4Format = "%2s";
    private final String formatInfo = " " + column1Format + " " + column2Format + " " + column3Format + " " + column4Format + System.lineSeparator();

    public CLIView(){
        askConnectionSettings();
    }

    public void askConnectionSettings(){
        ip = CLIView.askString("Insert server ip address:");
        boolean choice = CLIView.askYesNo("Default port is 7777. Do you want to change it?", false);
        port = (choice) ? CLIView.askNumber("Insert port number", 0, 65535) : 7777;
    }

    @Override
    public void startConnection() {
        for (boolean reachable = false; !reachable;) {
            try{
                serverHandler = new ServerHandler(new Socket(ip, port), this);
                System.out.println("You are connected to the server!");
                Client.logger.info("Connection established with server [" + ip + "]");
                new Thread(serverHandler).start();
                reachable = true;
            } catch(IOException e) {
                showErrorMessage("Server unreachable, try again.");
                Client.logger.info("Cannot reach server [" + ip + "]");
                askConnectionSettings();
            }
        }
    }

    @Override
    public void endGame(){
        System.out.println("Game over.");
        serverHandler.setRunning(false);
        int choice = askChoice("Do you want to:", "Exit game", "Start new game", "Start new game on different server");

        if (choice == 2){
            startConnection();
        }
        else if (choice == 3){
            askConnectionSettings();
            startConnection();
        }
        Client.logger.info("Closing client");
    }

    private static int inputNumber(int min, int max){
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice < min || choice > max)
            throw new InputMismatchException();
        return choice;
    }

    public static int askNumber(String text, int min, int max){

        if (min > max)
            throw new IllegalStateException();
        System.out.println(text + " [" + min + ((min == max) ? "]" : ("-" + max + "]")));
        try {
            return inputNumber(min, max);
        }catch (InputMismatchException e) {
            System.out.println(Graphics.ANSI_RED + "Invalid input" + Graphics.ANSI_RESET);
            return askNumber(text, min, max);
        }
    }

    public static int askChoice (String text, String... options){

        if (options == null)
            throw new IllegalArgumentException();

        System.out.println(text);
        for (int i = 0; i < options.length; i++)
            System.out.println(i + 1 + ")" + options[i]);

        try {
            return inputNumber(1, options.length);
        } catch (InputMismatchException e) {
            System.out.println(Graphics.ANSI_RED + "Invalid input" + Graphics.ANSI_RESET);
            return askChoice(text, options);
        }
    }

    public static String askChoiceValue(String text, String... options){
        return options[askChoice(text, options) - 1];
    }

    public static boolean askYesNo(String text, boolean defaultYes) {
        System.out.println(text + (defaultYes ? " [Y/n]" : " [y/N]"));
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        String choice = input.nextLine();
        if (choice.equalsIgnoreCase("y") || (choice.equals("") && defaultYes))
            return true;
        else if(choice.equalsIgnoreCase("n") || choice.equals(""))
            return false;
        else {
            System.out.println(Graphics.ANSI_RED + "Invalid input" + Graphics.ANSI_RESET);
            return askYesNo(text, defaultYes);
        }
    }

    public static String askString(String text){
        Scanner input = new Scanner(System.in);
        System.out.println(text);
        System.out.print(">");
        return input.nextLine();
    }

    @Override
    public SimpleModel getGame() { return game; }

    @Override
    public String getUsername() {
        String name = askString("Insert your username:");
        ThreadContext.put("name", name);
        return name;
    }

    /**
     * In this method the player insert the number of players for the game wanted.
     * @return the number of players.
     */
    public int getNumberOfPlayers(){
        int choice = askNumber("Insert the number of players for your game", 1, 4);
        showTextMessage("Waiting for other players to join the game...");
        return choice;
    }

    /**
     * This method shows the title of the game, the link where to download the rulebook as pdf and the legend of the graphic
     * symbols used as resources
     */
    @Override
    public void showTitle() {
        System.out.println(Graphics.ANSI_GREEN+Graphics.TITLE+Graphics.ANSI_RESET);
        showTextMessage("Game started");
        System.out.println(Graphics.ANSI_YELLOW+"Rules: "+ "https://tinyurl.com/mor-rules"+Graphics.ANSI_RESET);
        showLegend();
    }

    public void showLegend() {
        System.out.println("Legend: "
                +Graphics.getResource(Resource.FAITH)+"-Faith, "
                +Graphics.getResource(Resource.COIN)+ "-Coin, "
                +Graphics.getResource(Resource.SERVANT)+ "-Servant, "
                +Graphics.getResource(Resource.SHIELD)+ "-Shield, "
                +Graphics.getResource(Resource.STONE)+ "-Stone");
    }

    /**
     * This method shows a leader card
     * @param card is the card to show
     */
    public void showLeaderCard(SimpleLeaderCard card) {

        if (!card.isActive()) System.out.print(Graphics.ANSI_GREY);
        System.out.println("┌─────────┐"+Graphics.ANSI_RESET);

        if (!card.getResourceRequirements().isEmpty())
            showResources(card.getResourceRequirements());

        String[] cardReqs = new String[]{"","","",""};
        int i = 0;
        for (SimpleCardRequirement req : card.getCardRequirements()) {
            if (req.getLevel() == null) {
                cardReqs[i] = Graphics.getCardColor(req.getColor()) + req.getQuantity() + "■" + Graphics.ANSI_RESET;
            } else {
                cardReqs[i] = Graphics.getCardColor(req.getColor()) + "|" + req.getLevel() + "|" + Graphics.ANSI_RESET;
            }
            i++;
            if (i == 4) break;
        }
        if (i != 0)
            System.out.format(formatInfo, cardReqs[0], cardReqs[1], cardReqs[2], cardReqs[3]);

        System.out.println(Graphics.getAbility(card.getAbility().getType(), card.getAbility().getResource()));

        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);

        if (!card.isActive()) System.out.print(Graphics.ANSI_GREY);
        System.out.println("└─────────┘"+Graphics.ANSI_RESET);
    }

    /**
     * This method shows all the leader cards played by <code>player</code>. if <code>player</code> is the player
     * who is playing, it shows also the leader cards not played yet.
     * @param player is the player whose leader cards are shown.
     */
    public void printLeaderCards(SimplePlayer player){
        for (int i = 0; i < player.getLeaderCards().size(); i++) {
            System.out.println(i + 1 + ")");
            showLeaderCard(player.getLeaderCards().get(i));
        }
    }

    /**
     * This method is used when a player wants to discard a leader card. Its leader cards are shown and it chooses
     * the card to discard.
     * @return the id of the card to discard
     */
    @Override
    public CommandMsg discardLeaderCard() {

        SimplePlayer player = getThisPlayer();
        if(player.getLeaderCards().isEmpty())
            throw new IllegalStateException();
        else
            printLeaderCards(player);

        int choice = askNumber("Choose leaderCard to discard", 1, player.getLeaderCards().size());
        return new DiscardLeaderCardMsg(player.chooseLeaderCard(choice));
    }

    public void showMarbleBuffer(List<Marble> marbleList) {
        System.out.println("Marble Buffer:");
        for (Marble marble : marbleList) {
            System.out.print(Graphics.getMarble(marble)+" ");
        }
        System.out.println();
    }

    /**
     * This method is used to ask the player to choose a marble to put in a depot.
     * @return the marble chosen
     */
    @Override
    public Marble selectMarble(){
        showMarbleBuffer(game.getMarbleBuffer());
        int choice = askNumber("Choose a marble", 1, game.getMarbleBuffer().size());
        return  game.getMarbleBuffer().get(choice - 1);
    }

    /**
     * This method is used to ask the player to choose a depot in which to place the selected resource
     * @return the number of the depot chosen
     */
    @Override
    public DepotName selectDepot(){

        showWarehouse(getThisPlayer());
        List<SimpleDepot> depots = getThisPlayer().getWarehouse().getDepots();

        int intChoice = askChoice("Choose a depot in which to place the resource:",
                depots.stream().map(i -> i.getName().toString()).toArray(String[]::new));
        DepotName choice;
        if (intChoice == 1)         choice = DepotName.HIGH;
        else if (intChoice == 2)    choice = DepotName.MEDIUM;
        else if (intChoice == 3)    choice = DepotName.LOW;
        else if (intChoice == 4)    choice = DepotName.FIRST_EXTRA;
        else                        choice = DepotName.SECOND_EXTRA;

        return choice;
    }

    public Resource selectResource(){

        List<Resource> aliases = new ArrayList<>(getThisPlayer().getWhiteMarbleAliases());
        int choice = askChoice("Cast the white marble into one of the following resources:",
                                aliases.stream().map(Enum::name).toArray(String[]::new));

        return aliases.get(choice - 1);
    }

    @Override
    public void showTextMessage(String text) {
        System.out.println(Graphics.ANSI_BLUE+text+Graphics.ANSI_RESET);
    }

    /**
     * This method is used during the turn to ask the player to choose the action to do.
     * @param postTurn is a boolean which tells if the fame is in post turn state (when true) or not. When in post
     *                 turn the first action that can be done is to end the turn, while if not in post turn the first action
     *                 possible is a normal action.
     * @return the command msg of the action chosen.
     */
    @Override
    public CommandMsg selectMove(boolean postTurn) {

        int choice = askChoice("Select action: ", postTurn ? "EndTurn" : "Normal action", "Leader card action", "Show...");
        if (choice == 1) {
            if(postTurn)
                return new EndTurnMsg();
            else {
                int secondChoice = askChoice("Select action: ","Buy resources", "Buy development card", "Activate production", "Back...");
                if (secondChoice == 1)
                    return buyResources();
                else if (secondChoice == 2)
                    return buyCard();
                else if (secondChoice == 3)
                    return activateProduction(new HashSet<>(), new HashMap<>());
                else if (secondChoice == 4)
                    return selectMove(false);
            }
        }
        else if (choice == 2) {
            int secondChoice = askChoice("Select action:", "Play leader card", "Discard leader card", "Show my leader cards", "Back...");
            if (secondChoice == 1 && !getThisPlayer().getLeaderCards().isEmpty())
                return playLeaderCard();
            else if (secondChoice == 2 && !getThisPlayer().getLeaderCards().isEmpty())
                return discardLeaderCard();
            else if (secondChoice == 3){
                printLeaderCards(getThisPlayer());
                return selectMove(postTurn);
            }
            else
                return selectMove(postTurn);
        }
        else if (choice == 3){
            show();
            return selectMove(postTurn);
        }
        return null;
    }

    public CommandMsg manageResource(){

        CommandMsg msg;
        showMarbleBuffer(game.getMarbleBuffer());
        int choice = askChoice("Select action", "Insert resource in depot", "Discard resource", "Rearrange Depots", "Show...");

        if (choice == 1){
            Marble selectedMarble = selectMarble();
            DepotName selectedDepot = selectDepot();
            if (selectedMarble == Marble.WHITE) {
                Resource selectedResource = selectResource();
                msg = new PutResourceMsg(selectedMarble, selectedDepot, selectedResource);
            }
            else
                msg = new PutResourceMsg(selectedMarble, selectedDepot);
        }
        else if (choice == 2){
            Marble selectedMarble = selectMarble();
            msg = new DiscardMarbleMsg(selectedMarble);
        }
        else if (choice == 3)
            return changeDepots();
        else{
            show();
            return manageResource();
        }

        if (askYesNo("Confirm operation?", true))
            return msg;
        else
            return manageResource();
    }

    /**
     * This method shows graphic parts of the game required by the player. It works client side only, accessing the
     * simple model without communication with the server.
     */
    private void show() {
        String[] fixed = new String[] {"Resources legend", "Market board", "Decks", "Faith track"};
        String[] mutable = game.getPlayers().stream().map(i -> i.getUsername() + "'s playerboard").toArray(String[]::new);
        String[] options = Stream.concat(Arrays.stream(fixed), Arrays.stream(mutable)).toArray(String[]::new);
        int choice = askChoice("Select what to show:", options);

        switch (choice) {
            case 1-> showLegend();
            case 2 -> showMarketBoard();
            case 3-> showDevCardDecks();
            case 4 -> showFaithTrack();
            default -> showPlayerBoard(game.getPlayers().get(choice - 5));
        }
    }

    private void showPlayerBoard(SimplePlayer player) {

        if(player == null) throw new IllegalStateException();
        showWarehouse(player);
        showTextMessage("Strongbox:");
        System.out.println("----------------");
        showResources(player.getStrongbox());
        System.out.println("----------------");
        showTextMessage("Leader cards:");
        printLeaderCards(player);
        showTextMessage("Slots:");
        List<SimpleSlot> slots = player.getSlots();
        for (int i = 0; i < slots.size(); i++) {
            System.out.println("Slot "+ (i+1) +":");
            for (SimpleDevCard card : slots.get(i).getCards()) {
                showDevCard(card);
            }
            System.out.println("-------------------");
        }
    }

    private void showFaithTrack(){
        for (SimplePlayer player: game.getPlayers()) {
            System.out.println(player.getUsername()+" position: "+player.getPosition());
            System.out.print(player.getUsername()+" tiles: ");
            for (PopeFavourTile tile : player.getTiles()){
                if (!tile.isGained()) System.out.print(Graphics.ANSI_GREY);
                System.out.print("[" + tile.getValue() + "] ");
            }
            System.out.println(Graphics.ANSI_RESET);
        }
        if (game.getRivalPosition() != null)
            System.out.println("Lorenzo il Magnifico position: " + game.getRivalPosition());
        Graphics.showPositions();
    }

    /**
     * This method shows the top cards of the development card decks. The first index of the array is the level of the deck,
     * the second index is the color and the third is the position of the cards in the deck.
     */
    private void showDevCardDecks() {
        for (SimpleDevCard card: game.getDevCardDecks()) {
            System.out.println((game.getDevCardDecks().indexOf(card)+1)+")");
            showDevCard(card);
        }
    }

    public void showWarehouse(SimplePlayer player) {

        showTextMessage(player.getUsername() + " warehouse");

        for (SimpleDepot depot : player.getWarehouse().getDepots()) {
            System.out.print(depot.getName());
            System.out.println((depot.getConstraint() != null) ? ("(" + Graphics.getResource(depot.getConstraint()) + ")") : "");
            if (depot.getResource() != null && depot.getQuantity() != 0)
                showResources(Map.of(depot.getResource(), depot.getQuantity()));
            System.out.println("____________");
        }
    }

    @Override
    public CommandMsg changeDepots(){

        showWarehouse(getThisPlayer());
        int depot1;
        int depot2;
        List<String> depotList = new ArrayList<>();
        depotList.add("HIGH");
        depotList.add("MEDIUM");
        depotList.add("LOW");

        int pos = getThisPlayer().getWarehouse().getDepots().size();
        if(pos > 3) depotList.add("FIRST_EXTRA");
        if(pos > 4) depotList.add("SECOND_EXTRA");

        depot1 = askChoice("Choose first Depot", depotList.toArray(new String[pos]));

        DepotName depotName1 = DepotName.valueOf(depotList.get(depot1-1));
            depotList.remove(depot1-1);
            pos--;

        depot2 = askChoice("Choose second Depot", depotList.toArray(new String[pos]));

        DepotName depotName2 = DepotName.valueOf(depotList.get(depot2-1));
        depotList.clear();

        if (depotName1 == DepotName.FIRST_EXTRA || depotName1 == DepotName.SECOND_EXTRA
            || depotName2 == DepotName.FIRST_EXTRA || depotName2 == DepotName.SECOND_EXTRA){
            return new MoveDepotsMsg(depotName1, depotName2);
        } else
            return new SwitchDepotsMsg(depotName1,depotName2);
    }


    private CommandMsg playLeaderCard(){
        SimplePlayer player = getThisPlayer();
        if (player.getLeaderCards().isEmpty())
            throw new IllegalStateException();
        else{
            printLeaderCards(player);
            int position = askNumber("Choose leader card to play", 1, player.getLeaderCards().size());
            int cardId = player.chooseLeaderCard(position);
            return new PlayLeaderCardMsg(cardId);
        }
    }

    private CommandMsg buyResources(){

        showMarketBoard();
        int choice = askChoice("Do you want to buy a:", "Column", "Row", "Back...");

        if(choice == 3)
            return selectMove(false);

        boolean isRow;
        isRow = choice != 1;

        choice = askNumber("Insert the number of " + (isRow ? "row:":"column:"), 1, (isRow ? 3 : 4));
        return new BuyResourcesMsg(choice - 1, isRow);
    }

    private CommandMsg activateProduction(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){

        if (selectedCardIds == null || selectedExtraPowers == null)
            throw new IllegalArgumentException();

        int choice = askChoice("Select action:", "Select development card", "Select special production power", "Show selected", "Reset", "Confirm", "Back...");

        if (choice == 1) {
            selectDevCard(selectedCardIds).map(selectedCardIds::add);
            return activateProduction(selectedCardIds, selectedExtraPowers);
        }
        else if (choice == 2){
            selectExtraPower(selectedExtraPowers).map(i -> selectedExtraPowers.put(i.getKey(), i.getValue()));
            return activateProduction(selectedCardIds, selectedExtraPowers);
        }
        else if (choice == 3){
            showProductionSelection(selectedCardIds, selectedExtraPowers);
            return activateProduction(selectedCardIds, selectedExtraPowers);
        }
        else if (choice == 4)
            return activateProduction(new HashSet<>(), new HashMap<>());
        else if (choice == 5)
            if (!selectedCardIds.isEmpty() || !selectedExtraPowers.isEmpty())
                return new ActivateProductionMsg(selectedCardIds, selectedExtraPowers);
            else return activateProduction(selectedCardIds, selectedExtraPowers);

        return selectMove(false);
    }

    private void showProductionSelection(Set<Integer> selectedCardIds, Map<Integer, ProductionPower> selectedExtraPowers){
        System.out.println("Selected development cards:");
        for (Integer i : selectedCardIds)
            showDevCard(SimpleDevCard.parse(i));
        System.out.println("Selected extra powers:");
        for (Integer i : selectedExtraPowers.keySet()) {
            Map<Resource, Integer> totalInput = new HashMap<>();
            Map<Resource, Integer> totalOutput = new HashMap<>();
            getThisPlayer().getExtraProductionPowers().get(i).getInput().forEach(totalInput::put);
            getThisPlayer().getExtraProductionPowers().get(i).getOutput().forEach(totalOutput::put);
            selectedExtraPowers.get(i).getInput().forEach((k, v) -> totalInput.merge(k, v, Integer::sum));
            selectedExtraPowers.get(i).getOutput().forEach((k, v) -> totalOutput.merge(k, v, Integer::sum));
            showRealProductionPower(new ProductionPower(totalInput, totalOutput));
        }
    }

    private Optional<Integer> selectDevCard(Set<Integer> selectedCardIds) {

        SimpleDevCard[] availableCards = getThisPlayer().getSlots().stream()
                .map(SimpleSlot::getLastCard).flatMap(Optional::stream)
                .filter(i -> !selectedCardIds.contains(i.getId())).toArray(SimpleDevCard[]::new);
        if (availableCards.length == 0){
            showErrorMessage("No available cards");
            return Optional.empty();
        }

        System.out.println("Available cards:");
        for (int i = 0; i < availableCards.length; i++) {
            System.out.println(i + 1 + ")");
            showDevCard(availableCards[i]);
        }
        int choice = askNumber("Select development card", 1, availableCards.length);
        return Optional.of(availableCards[choice - 1].getId());
    }

    private Optional<Map.Entry<Integer, ProductionPower>> selectExtraPower(Map<Integer, ProductionPower> selectedExtraPowers) {

        System.out.println("Available extra powers:");
        List<ProductionPower> extraPowers = getThisPlayer().getExtraProductionPowers();
        List<ProductionPower> availablePowers = extraPowers.stream()
                .filter(i -> !selectedExtraPowers.containsKey(extraPowers.indexOf(i))).collect(Collectors.toList());

        if (availablePowers.size() == 0) {
            showErrorMessage("No available extra powers");
            return Optional.empty();
        }

        for (int i = 0; i < availablePowers.size(); i++){
            System.out.println(i + 1 + ")");
            showRealProductionPower(availablePowers.get(i));
        }
        int choice = askNumber("Select power",1, availablePowers.size());
        ProductionPower chosenPower = availablePowers.get(choice - 1);
        Map<Resource, Integer> realInput = new HashMap<>();
        Map<Resource, Integer> realOutput = new HashMap<>();
        for (int i = 0; i < chosenPower.getAgnosticInput(); i++){
            Resource inputChoice = Resource.valueOf(askChoiceValue("Choose an input resource:", "COIN", "SERVANT", "SHIELD", "STONE"));
            if (!realInput.containsKey(inputChoice))
                realInput.put(inputChoice, 1);
            else
                realInput.put(inputChoice, realInput.get(inputChoice) + 1);
        }
        for (int i = 0; i < chosenPower.getAgnosticOutput(); i++){
            Resource outputChoice = Resource.valueOf(askChoiceValue("Choose an output resource", "COIN", "SERVANT", "SHIELD", "STONE"));
            if (!realOutput.containsKey(outputChoice))
                realOutput.put(outputChoice, 1);
            else
                realOutput.put(outputChoice, realInput.get(outputChoice) + 1);
        }
        return Optional.of(new AbstractMap.SimpleEntry<>(extraPowers.indexOf(chosenPower), new ProductionPower(realInput, realOutput)));
    }

    private void showRealProductionPower(ProductionPower power){

        System.out.print(" Input: ");
        if (power.getAgnosticInput() != 0)
            System.out.print(power.getAgnosticInput() + "?");
        for (Map.Entry<Resource, Integer> entry : power.getInput().entrySet())
            System.out.print(" " + entry.getValue() + Graphics.getResource(entry.getKey()));
        System.out.println();

        System.out.print(" Output: ");
        if (power.getAgnosticOutput() != 0)
            System.out.print(power.getAgnosticOutput() + "?");
        for (Map.Entry<Resource, Integer> entry : power.getOutput().entrySet())
            System.out.print(" " + entry.getValue() + Graphics.getResource(entry.getKey()));
        System.out.println();
        System.out.println();
    }

    private CommandMsg buyCard(){

        showDevCardDecks();
        int position = askNumber("Choose developement card to buy", 1, game.getDevCardDecks().size());

        int slotIdx = -1 + askNumber("Choose the slot where you want to insert the development card", 1, getThisPlayer().getSlots().size());

        CardColor cardColor = game.getDevCardDecks().get(position-1).getColor();
        int level =  game.getDevCardDecks().get(position-1).getLevel();

        return new BuyAndAddCardInSlotMsg(cardColor, level, slotIdx);
    }

    @Override
    public void showLeaderboard(Map<String, Integer> leaderboard) {
        System.out.println("Leaderboard:");
        int count = 1;
        for (String username : leaderboard.keySet()) {
            System.out.println(count + ") " + username + " [" + leaderboard.get(username) + "]");
            count++;
        }
    }

    public void showDevCard(SimpleDevCard card) {
        System.out.println("┌──────────┐");
        //Level and color
        System.out.println("    "+Graphics.getLevel(card.getColor(), card.getLevel()));
        showResources(card.getRequirements());
        showProductionPower(card.getColor(), card.getInput(), card.getOutput());
        //victory points
        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");
    }

    public void showProductionPower(CardColor color, Map<Resource, Integer> input, Map<Resource, Integer> output){
        System.out.println(Graphics.getCardColor(color)+"Input:"+Graphics.ANSI_RESET);
        String[] inputString = new String[2];
        int i = 0;
        for(Resource inRes : input.keySet()) {
            inputString[i] = input.get(inRes) +  " " + Graphics.getResource(inRes);
            i++;
        }
        if(inputString[1]==null) inputString[1]="";
        System.out.format(formatInfo, inputString[0], "", inputString[1], "");

        System.out.println(Graphics.getCardColor(color) + "Output:" + Graphics.ANSI_RESET);
        String[] outputString = new String[3];
        i=0;
        for(Resource outRes : output.keySet()) {
            outputString[i] = output.get(outRes) + " " + Graphics.getResource(outRes);
            i++;
        }
        for(int j=0; j<3; j++) {
            if(outputString[j]==null)
                outputString[j]="";
        }

        System.out.format(formatInfo, outputString[0], outputString[1], outputString[2], "");
    }

    public void showResources(Map<Resource, Integer> resources) {
        if(resources == null || resources.isEmpty()){
            System.out.println();
            return;
        }
        String[] req = new String[]{"","","",""};
        int i = 0;
        for (Resource res: resources.keySet()) {
            req[i] = (resources.get(res) + Graphics.getResource(res));
            i++;
        }
        System.out.format(formatInfo, req[0], req[1], req[2], req[3]);
    }

    public void showMarketBoard() {
        Marble[][] marketBoard = game.getMarketBoard();
        System.out.println("  1 2 3 4");
        for(int i=0; i<3; i++)
        {
            System.out.print((i+1) + " ");
            for (int j=0; j<4; j++)
            {
                System.out.print(Graphics.getMarble(marketBoard[i][j])+" ");
            }
            System.out.print("\n");
        }
        System.out.print("Spare marble: ");
        System.out.print(Graphics.getMarble(game.getSpareMarble()));
        System.out.print("\n");
    }

    @Override
    public void showErrorMessage(String text) {
        System.out.println(Graphics.ANSI_RED + text + Graphics.ANSI_RESET);
    }

    public SimplePlayer getThisPlayer(){
        return game.getThisPlayer();
    }

    @Override
    public void setModel(SimpleModel model){
        this.game = model;
        serverHandler.setModel(model);
    }
}
