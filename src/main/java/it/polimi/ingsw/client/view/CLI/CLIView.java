package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.SimpleDevelopmentCard;
import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimplePlayer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import it.polimi.ingsw.messages.command.BuyResourcesMsg;
import it.polimi.ingsw.messages.command.CommandMsg;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class CLIView implements View {
    private CLISettingView settingView;
    private SimpleGame game;

    private String column1Format = "%-2s";    // min 3 characters, left aligned
    private String column2Format = "%-2s";  // min 5 and max 8 characters, left aligned
    private String column3Format = "%2s";   // fixed size 6 characters, right aligned
    private String formatInfo = column1Format + " " + column2Format + " " + column3Format;


    public CLIView() {
        this.settingView = new CLISettingView(this);
        game = new SimpleGame(this);
    }

    public SimpleGame getGame() {
        return game;
    }

    @Override
    public void positionUpdate(SimplePlayer player) {

    }

    @Override
    public void bufferUpdate(Marble marble) {

    }

    @Override
    public void showErrorMessage(String text) {
        System.out.println(Graphics.ANSI_RED+"ATTENTION!");
        System.out.println(text);
    }

    @Override
    public void showConfirmMessage(String text) {
        System.out.println(Graphics.ANSI_GREEN+"OK!");
        System.out.println(text);
    }

    @Override
    public void faceUpLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void discardLeaderCard(SimplePlayer player, int cardId) {

    }

    @Override
    public void showLeaderCardAllPlayers(int cardId) {

    }

    @Override
    public String getUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert your username:");
        System.out.print(Graphics.ANSI_CYAN+">");
        return input.nextLine();
    }

    public int getNumber(){
        Scanner input = new Scanner(System.in);
        System.out.println(Graphics.ANSI_RESET+"Insert the number of players for your game:");
        System.out.print(Graphics.ANSI_CYAN+">");
        return input.nextInt();
    }

    @Override
    public void startGame() {
        System.out.println(Graphics.ANSI_GREEN+Graphics.TITLE);
        System.out.println("Game started");
    }

    @Override
    public void startSetting() {
        settingView.execute();
    }

    @Override
    public void showDevCardAllPlayers(int cardId) {

    }

    @Override
    public void addCardInSlot(SimplePlayer player, int cardId, int cardSlot) {

    }

    @Override
    public CommandMsg selectMove() {
        System.out.println("Select your move:");
        System.out.println("1) Buy resources");
        System.out.println("2) Buy development card");
        System.out.println("3) Activate production");
        System.out.print(">");
        Scanner input = new Scanner(System.in);
        int choice = 0;
        try{
            choice=input.nextInt();
            if(choice<1||choice>3) throw new InputMismatchException();
        }catch (Exception e)
        {
            showErrorMessage("Illegal input");
            selectMove();
        }
        switch (choice){
            case 1 -> {
                return buyResources();
            }
            case 2-> {
                return buyCard();
            }
            case 3->{
                return activateProduction();
            }
            default ->{
                return null;
            }
        }
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
        return null;
    }
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
        showRequirements(card.getRequirements());
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
        System.out.format(formatInfo, input[0], "", input[1]);
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

        System.out.format(formatInfo, output[0], output[1], output[2]);
        System.out.println();

        //vicotry points
        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");
    }

    private void showRequirements(Map<Resource, Integer> requirements)
    {
        String[] req = new String[3];
        int i = 0;
        for(Resource res: requirements.keySet())
        {
            req[i] = requirements.get(res)+ " " + Graphics.getResource(res);
            i++;
            //System.out.println("│ "+card.getRequirement().get(res)+ " " + Graphics.getResource(res));
        }
        for(int j=0; j<3; j++)
        {
            if(req[j]==null)
                req[j]="";
        }
        System.out.format(formatInfo, req[0], req[1], req[2]);
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
    }

    public void showLeaderCard(SimpleLeaderCard card)
    {
        System.out.println("┌──────────┐");
        if(card.getResourceRequirements()!=null)
            showRequirements(card.getResourceRequirements());
        if(!card.isLevelRequired()){ //if the integer represents the number of cards
            String [] req = new String[2];
            int i =0;
            for (CardColor color :card.getCardRequirements().keySet()) {
                req[i] = Graphics.getCardColor(color)+card.getCardRequirements().get(color) +"■"+Graphics.ANSI_RESET;
                i++;
            }
            if(req[1]==null) req[1]="";
            System.out.format(formatInfo, req[0], "", req[1]);
            System.out.println();
        }
        else //if the integer represents the level of the card
        {
            for (CardColor color:card.getCardRequirements().keySet()) {
                System.out.println(Graphics.getCardColor(color)+" |"+card.getCardRequirements().get(color)+"|"+Graphics.ANSI_RESET);
            }
        }
        System.out.println(Graphics.getAbility(card.getAbility(), card.getAbilityResource()));


        //vicotry points
        System.out.println("     "+Graphics.ANSI_YELLOW+card.getVictoryPoints()+Graphics.ANSI_RESET);
        System.out.println("└──────────┘");
    }

    public void setGame(SimpleGame game) {
        this.game = game;
    }
}
