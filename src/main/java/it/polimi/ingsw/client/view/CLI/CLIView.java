package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.command.*;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;

import java.sql.SQLOutput;
import java.util.*;

public class CLIView implements View {
    private CLISettingView settingView;
    private SimpleGame game;

    private final String column1Format = "%-2s";
    private final String column2Format = "%-2s";
    private final String column3Format = "%2s";
    private final String formatInfo = column1Format + " " + column2Format + " " + column3Format+ " "+ column3Format;


    public CLIView() {
        this.settingView = new CLISettingView(this);
        game = new SimpleGame(this);
    }

    @Override
    public SimpleGame getGame() {
        return game;
    }

    @Override
    public void startSetting() {
        settingView.execute();
    }

    @Override
    public String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert your username:");
        System.out.print(Graphics.ANSI_CYAN+">");
        return input.nextLine();
    }

    /**
     * In this method the player insert the number of players for the game wanted.
     * @return the number of players.
     */
    public int getNumber(){
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert the number of players for your game:");
        System.out.print(Graphics.ANSI_CYAN+">");
        int result;
        try{
            result=input.nextInt();
            if(result<1||result>4) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Invalid input");
            return getNumber();
        }
        if (result!=1)
            System.out.println(Graphics.ANSI_BLUE+"Waiting for other players to join the game..."+Graphics.ANSI_RESET);
        return result;
    }

    /**
     * This method shows the title of the game, the link where to download the rulebook as pdf and the legend of the graphic
     * symbols used as resources
     */
    @Override
    public void startGame() {
        System.out.println(Graphics.ANSI_GREEN+Graphics.TITLE+Graphics.ANSI_RESET);
        System.out.println(Graphics.ANSI_BLUE+"Game started"+Graphics.ANSI_RESET);
        System.out.println(Graphics.ANSI_YELLOW+"Rules: "+ "https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjrp562xsLwAhVQ4YUKHZWMBhcQFjAAegQIAhAD&url=http%3A%2F%2Fboardgame.bg%2Fmasters%2520of%2520renaissance%2520rules.pdf&usg=AOvVaw3zG_mi_quuZtRXse1AERmk"+Graphics.ANSI_RESET);
        System.out.println("Legend: "
                +Graphics.getResource(Resource.FAITH)+"-Faith, "
                +Graphics.getResource(Resource.COIN)+ "-Coin, "
                +Graphics.getResource(Resource.SERVANT)+ "-Servant, "
                +Graphics.getResource(Resource.SHIELD)+ "-Shield, "
                +Graphics.getResource(Resource.STONE)+ "-Stone");
        System.out.println(Graphics.ANSI_CYAN+"Waiting for your turn to choose the leader cards..."+Graphics.ANSI_RESET);
    }

    /**
     * This method shows a leader card
     * @param card is the card to show
     */
    public void showLeaderCard(SimpleLeaderCard card)
    {

        System.out.println(Graphics.ANSI_RESET+ "┌──────────┐");
        if(card.getResourceRequirements()!=null &&!card.getResourceRequirements().isEmpty())
            showResources(card.getResourceRequirements());
        String[] req = new String[2];
        int i =0;
        if(card.getCardRequirements()!= null && !card.getCardRequirements().isEmpty()) {
            for (CardColor color : card.getCardRequirements().keySet()) {
                for (Integer level : card.getCardRequirements().get(color).keySet()) {
                    if (level == null) {
                        req[i] = Graphics.getCardColor(color) + card.getCardRequirements().get(color).get(level) + "■" + Graphics.ANSI_RESET;
                        i++;
                    } else {
                        req[i] = Graphics.getCardColor(color) + " |" + level + "|" + Graphics.ANSI_RESET;
                    }
                }
            }
            if (req[1] == null) req[1] = "";
            System.out.format(formatInfo, req[0], "", req[1], "");
            System.out.println();
        }
        System.out.println(Graphics.getAbility(card.getAbility(), card.getAbilityResource()));


        //vicotry points
        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");
    }

    /**
     * This method shows all the leader cards played by <code>player</code>. if <code>player</code> is the player
     * who is playing, it shows also the leader cards not played yet.
     * @param player is the player whose leader cards are shown.
     */
    @Override
    public void printLeaderCards(SimplePlayer player){
        int counter = 1;
        boolean showallCards = false;
        if(player.getUsername().equals(game.getThisPlayer())) showallCards = true;

        for (SimpleLeaderCard card : player.getLeaderCards()) {
            if(showallCards || card.isActive()) {
                System.out.println(Graphics.ANSI_RESET+ counter + ")");
                showLeaderCard(card);
                counter++;
            }
        }
    }

    /**
     * This method is used when a player wants to discard a leader card. Its leader cards are shown and it chooses
     * the card to discard.
     * @return the id of the card to discard
     */
    @Override
    public int getDiscardedLeaderCard() {

        SimplePlayer player;
        boolean valid = false;

        player = findPlayer(game.getThisPlayer());

        if(player.getLeaderCards().isEmpty())return 0;
            else printLeaderCards(player);

            int position = 0;
        Scanner input = new Scanner(System.in);

        while(!valid) {
            System.out.println(Graphics.ANSI_RESET+"Choose leaderCard to discard ( number 1-" + player.getLeaderCards().size() + "):");
            System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
            position = input.nextInt();
            if(position > player.getLeaderCards().size() || position < 1){
                showErrorMessage("Insert a number between 1-" + player.getLeaderCards().size());
            }
            else
                valid = true;
        }

        return player.chooseLeaderCard(position);
    }


    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showMarbleBuffer(List<Marble> marbleList) {
        System.out.println("Marble Buffer:");
        for (Marble marble : marbleList)
        {
            System.out.print(Graphics.getMarble(marble)+" ");
        }
    }

    /**
     * This method is used to ask the player to choose a marble to put in a depot.
     * @return the marble chosen
     */
    @Override
    public Marble selectedMarble(){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a marble to put in a depot (insert a number between 1-"+ game.getMarbleBuffer().size()+"):");
        System.out.print(Graphics.ANSI_CYAN+">");
        int position;
        try{
            position = input.nextInt();
            if(position<1||position>4) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Illegal input");
            return selectedMarble();
        }

        return  game.getMarbleBuffer().get(position-1);
    }

    /**
     * This method is used to ask the player to choose a depot in which to place the selected resource
     * @return the number of the depot chosen
     */
    @Override
    public int selectedDepot(){
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a depot in which to place the resource( 1->HIGH DEPOT, 2->MEDIUM DEPOT, 3->LOW DEPOT ):");
        System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
        int choice;
        try{
            choice=input.nextInt();
            if(choice<1||choice>3) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Illegal input");
            return selectedDepot();
        }
        return choice;
    }

    @Override
    public void showStatusMessage(String text) {
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
        String firstAction;
        if(!postTurn)
            firstAction="Normal action";
        else
            firstAction="End turn";
        System.out.println("Select what to do:");
        System.out.println("1) "+ firstAction);
        System.out.println("2) Leader card action");
        System.out.println("3) Show...");
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        int choice;
        try{
            choice=input.nextInt();
            if(choice<1||choice>3) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Illegal input");
            return selectMove(postTurn);
        }
        switch (choice){
            case 1 -> {
                if(postTurn)
                    return new EndTurnMsg();
                else
                System.out.println("Select the action:");
                System.out.println("1) Buy resources");
                System.out.println("2) Buy development card");
                System.out.println("3) Activate production");
                System.out.println("4) Back...");
                try{
                    choice=input.nextInt();
                    if(choice<1||choice>4) throw new InputMismatchException();
                }catch (Exception e)
                {
                    showErrorMessage("Illegal input");
                    return selectMove(postTurn);
                }
                switch (choice)
                {
                    case 1 -> {
                        return buyResources();
                    }
                    case 2 -> {
                        return buyCard();
                    }
                    case 3 -> {
                        return activateProduction();
                    }
                    case 4 -> {
                        return selectMove(postTurn);
                    }
                }
            }
            case 2-> {
                System.out.println("Select what to do: ");
                System.out.println("1) Play leader card");
                System.out.println("2) Discard leader card");
                System.out.println("3) Show my leader cards");
                System.out.println("4) Back...");
                System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);

                try{
                    choice=input.nextInt();
                    if(choice<1||choice>4) throw new InputMismatchException();
                }catch (Exception e)
                {
                    showErrorMessage("Illegal input");
                    return selectMove(postTurn);
                }
                switch (choice) {
                    case 1 -> { return playLeaderCard();}
                    case 2 -> { return discardLeaderCard(); }
                    case 3 -> {
                        printLeaderCards(findPlayer(game.getThisPlayer()));
                        return selectMove(postTurn);
                    }
                    case 4 -> { return selectMove(postTurn); }
                }
            }
            case 3->{
                show();
                return selectMove(postTurn);
            }
            default ->{
                return null;
            }
        }
        return null;
    }

    /**
     * This method shows graphic parts of the game required by the player. It works client side only, accessing the
     * simple model without communication with the server.
     */
    private void show()
    {
        System.out.println("Select what to show: ");
        System.out.println("1) market board");
        System.out.println("2) decks");
        System.out.println("3) faith track");
        Map<Integer, String> players = new HashMap<>();
        int i =4;
        for (SimplePlayer player: game.getPlayers()) {
            System.out.println(i + ") " + player.getUsername() + " player board");
            players.put(i, player.getUsername());
            i++;
        }
        System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
        Scanner input = new Scanner(System.in);
        int choice = 0;
        try{
            choice=input.nextInt();
            if(choice<1||choice>=i) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Invalid input");
            show();
        }
        switch (choice) {
            case 1 -> showMarketBoard();
            case 2-> showDevCardDecks();
            case 3 -> showFaithTrack();
            default -> showPlayerBoard(players.get(choice));
        }
    }

    private void showPlayerBoard(String name)
    {
        SimplePlayer player = null;
        for (SimplePlayer p: game.getPlayers()) {
            if(p.getUsername().equals(name))
            {
                player = p;
                break;
            }
        }
        if(player==null) throw new IllegalStateException();
        showWarehouse(player);
        System.out.println(Graphics.ANSI_BLUE+"Strongbox:"+Graphics.ANSI_RESET);
        System.out.println("----------------");
        showResources(player.getStrongbox());
        System.out.println("----------------");
        System.out.println(Graphics.ANSI_BLUE+"Leader cards:"+Graphics.ANSI_RESET);
        printLeaderCards(player);
        System.out.println(Graphics.ANSI_BLUE+"Slots:"+Graphics.ANSI_RESET);
        for (SimpleSlot slot:player.getSlots()) {
            System.out.println("Slot "+slot.getId()+":");
            for (SimpleDevelopmentCard card:slot.getCards()) {
                showCard(card);
            }
            System.out.println("-------------------");
        }


    }
    private void showFaithTrack(){
        for (SimplePlayer player: game.getPlayers()) {
            System.out.println(player.getUsername()+" position: "+player.getPosition());
        }
        Graphics.showPositions();
    }

    /**
     * This method shows the top cards of the development card decks. The first index of the array is the level of the deck,
     * the second index is the color and the third is the position of the cards in the deck.
     */
    private void showDevCardDecks() {
        for (SimpleDevelopmentCard card: game.getDevCardDecks()) {
            System.out.println((game.getDevCardDecks().indexOf(card)+1)+")");
            showCard(card);
        }
    }

    public void showWarehouse(SimplePlayer player)
    {
        SimpleWarehouse warehouse = player.getWarehouse();
        System.out.println(player.getUsername()+" warehouse");
        for (DepotName depot: warehouse.getDepots().keySet()) {
            showResources(warehouse.getDepots().get(depot));
            System.out.println("____________");
        }
    }


    private CommandMsg discardLeaderCard(){
        return new DiscardLeaderCardMsg(getDiscardedLeaderCard());
    }

    private CommandMsg playLeaderCard(){
        SimplePlayer player = findPlayer(game.getThisPlayer());
        int cardId = 0;

        if(!player.getLeaderCards().isEmpty()){
            printLeaderCards(player);

            boolean valid = false;
            int position = 0;

            while(!valid) {
                Scanner input = new Scanner(System.in);
                System.out.println(Graphics.ANSI_RESET+"Choose leaderCard to play ( number 1-" + player.getLeaderCards().size() + "):");
                System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
                position = input.nextInt();

                if(position > player.getLeaderCards().size() || position < 1){
                    showErrorMessage("Insert a number between 1-" + player.getLeaderCards().size());
                }
                else valid = true;
            }
            cardId = player.chooseLeaderCard(position);
        }

        return new PlayLeaderCardMsg(cardId);
    }

    private CommandMsg buyResources(){
        showMarketBoard();
        System.out.println("Want to buy a column/row?");
        System.out.println("1) column");
        System.out.println("2) row");
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        boolean valid = false;
        int choice = 0;
        while(!valid)
        {
            try {
                choice=input.nextInt();
                if(choice<1||choice>2) throw new InputMismatchException();
                valid=true;
            }catch (Exception e)
            {
                showErrorMessage("Invalid input");
            }
        }
        boolean isRow;
        isRow= choice != 1;
        System.out.println("Insert the number of the " + (isRow? "row:":"column:"));
        System.out.print(">");
        valid=false;
        while (!valid)
        {
            try
            {
                choice=input.nextInt();
                int bound = (isRow? 3:4);
                if(choice<1||choice>bound) throw new InputMismatchException();
                valid=true;
            }catch (Exception e)
            {
                showErrorMessage("Invalid input");
            }
        }
        return new BuyResourcesMsg(choice-1, isRow);
    }

    private CommandMsg activateProduction(){
        return null;
    }

    private CommandMsg buyCard(){
        boolean valid = false;
        showDevCardDecks();
        int position = 0;
        Scanner input = new Scanner(System.in);

        while(!valid) {

            System.out.println(Graphics.ANSI_RESET+"Choose developmentCard to but ( number 1-" + game.getDevCardDecks().size() + "):");
            System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
             position = input.nextInt();

            if(position > game.getDevCardDecks().size() || position < 1){
                showErrorMessage("Insert a number between 1-" + game.getDevCardDecks().size());
            }
            else valid = true;

        }

        valid = false;

        int slotId = 0;
        while(!valid){

            System.out.println(Graphics.ANSI_RESET+"Choose slot in which to insert the developmentCard( number 1-" +findPlayer(game.getThisPlayer()).getSlots().size() + "):");
            System.out.print(Graphics.ANSI_CYAN+">"+Graphics.ANSI_RESET);
            slotId = input.nextInt();

            if(slotId > findPlayer(game.getThisPlayer()).getSlots().size() || slotId < 1){
                showErrorMessage("Insert a number between 1-" + findPlayer(game.getThisPlayer()).getSlots().size());
            }
            else valid = true;

        }

        CardColor cardColor = game.getDevCardDecks().get(position-1).getColor();
        int level =  game.getDevCardDecks().get(position-1).getLevel();

        return new BuyandAddCardInSlotMsg(cardColor, level, slotId);
    }
    @Deprecated
    public void showCardLegend()
    {

        System.out.println("┌──────────┐");
        System.out.println(Graphics.ANSI_RED+"              <----Card color and level");
        System.out.println("              <----Card requirements");
        System.out.println();
        System.out.println("              <----Input resources");
        System.out.println("              <----Output resources");
        System.out.println("              <----Victory points"+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");

    }
    public void showCard(SimpleDevelopmentCard card)
    {
        System.out.println("┌──────────┐");
        //Level and color
        System.out.println("    "+Graphics.getLevel(card.getColor(), card.getLevel()));
        int i;
        showResources(card.getRequirements());
        //input
        System.out.println(Graphics.getCardColor(card.getColor())+"Input:"+Graphics.ANSI_RESET);
        //System.out.println();
        String[] input= new String[2];
        i=0;
        for(Resource inRes : card.getInput().keySet())
        {
            input[i] = card.getInput().get(inRes)+ " " +Graphics.getResource(inRes);
            i++;
        }
        if(input[1]==null) input[1]="";
        System.out.format(formatInfo, input[0], "", input[1], "");
        System.out.println();

        //output
        System.out.println(Graphics.getCardColor(card.getColor())+"Output:"+Graphics.ANSI_RESET);
        String[] output = new String[3];
        i=0;
        for(Resource outRes : card.getOutput().keySet())
        {
            output[i] = card.getOutput().get(outRes)+ " " +Graphics.getResource(outRes);
            i++;
        }
        for(int j=0; j<3; j++)
        {
            if(output[j]==null)
                output[j]="";
        }

        System.out.format(formatInfo, output[0], output[1], output[2], "");
        System.out.println();

        //vicotry points
        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");
    }

    public void showResources(Map<Resource, Integer> resources)
    {
        if(resources== null ||resources.isEmpty()){
            System.out.println("");
            return;
        }
        String[] req = new String[4];
        int i = 0;
        for(Resource res: resources.keySet())
        {
            req[i] = (resources.get(res)+ " " + Graphics.getResource(res));
            i++;

        }
        for(int j=0; j<4; j++)
        {
            if(req[j]==null)
                req[j]="";
        }
        System.out.format(formatInfo, req[0], req[1], req[2], req[3]);
        System.out.println();
    }

    public void showMarketBoard()
    {
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



    /**Auxiliary methods */

    @Override
    public void showErrorMessage(String text) {
        System.out.println(Graphics.ANSI_RED+"ATTENTION!");
        System.out.println(text+Graphics.ANSI_RESET);
    }


    @Override
    public void showConfirmMessage(String text) {
        System.out.println(Graphics.ANSI_GREEN+"OK!");
        System.out.println(text+Graphics.ANSI_RESET);
    }

    public void setGame(SimpleGame game) {
        this.game = game;
    }

    public SimplePlayer findPlayer(String username){
        for(SimplePlayer simplePlayer : game.getPlayers())
            if (simplePlayer.getUsername().equals(username)) return simplePlayer;

            return null;
    }



}
