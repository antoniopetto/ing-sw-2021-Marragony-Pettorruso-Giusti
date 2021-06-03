package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.SimpleAbility;
import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.server.model.cards.CardColor;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;

import java.util.Map;

public class Graphics {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\033[38;2;0;255;0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREY = "\033[38;2;105;105;105m";
    public static final String TITLE = "  __  __                 _                                    __     ____                           _                                           \n" +
            " |  \\/  |   __ _   ___  | |_    ___   _ __   ___      ___    / _|   |  _ \\    ___   _ __     __ _  (_)  ___   ___    __ _   _ __     ___    ___ \n" +
            " | |\\/| |  / _` | / __| | __|  / _ \\ | '__| / __|    / _ \\  | |_    | |_) |  / _ \\ | '_ \\   / _` | | | / __| / __|  / _` | | '_ \\   / __|  / _ \\\n" +
            " | |  | | | (_| | \\__ \\ | |_  |  __/ | |    \\__ \\   | (_) | |  _|   |  _ <  |  __/ | | | | | (_| | | | \\__ \\ \\__ \\ | (_| | | | | | | (__  |  __/\n" +
            " |_|  |_|  \\__,_| |___/  \\__|  \\___| |_|    |___/    \\___/  |_|     |_| \\_\\  \\___| |_| |_|  \\__,_| |_| |___/ |___/  \\__,_| |_| |_|  \\___|  \\___|\n" +
            "                                                                                                                                                ";
    public static String getResource(Resource res)
    {
        switch (res)
        {
            case COIN -> {
                return ANSI_YELLOW+"●"+ANSI_RESET;
            }
            case STONE -> {
                return ANSI_WHITE+"▲"+ANSI_RESET;
            }
            case SHIELD -> {
                return ANSI_CYAN+"♠"+ANSI_RESET;
            }
            case SERVANT -> {
                return ANSI_PURPLE+"♣"+ANSI_RESET;
            }
            case FAITH -> {
                return ANSI_RED+"†"+ANSI_RESET;
            }
        }
        return "";
    }
    public static String getCardColor(CardColor color)
    {
        String result="";
        switch (color){
            case BLUE -> result = ANSI_BLUE;
            case GREEN -> result = ANSI_GREEN;
            case PURPLE -> result= ANSI_PURPLE;
            case YELLOW -> result = ANSI_YELLOW;
        }
        return result;
    }
    public static String getLevel(CardColor color, int level)
    {
        String result = getCardColor(color);
        result+="■" + level+ "■" +ANSI_RESET;

        return result;
    }
    public static void showPositions()
    {
        System.out.println("Important positions:");
        System.out.println("(The number indicates the victory points, blue color indicates vatican report positions, red indicates pope space)");
        System.out.print("  3-" +Graphics.ANSI_YELLOW+"(1)"+Graphics.ANSI_RESET);
        System.out.print(Graphics.ANSI_BLUE+"  5");
        System.out.print("  6-"+Graphics.ANSI_YELLOW+"(2)");
        System.out.print(Graphics.ANSI_BLUE+"  7");
        System.out.print(Graphics.ANSI_RED+"  8"+Graphics.ANSI_RESET);
        System.out.print("  9-" +Graphics.ANSI_YELLOW+"(4)"+Graphics.ANSI_RESET);
        System.out.print(Graphics.ANSI_BLUE+"  12-"+Graphics.ANSI_YELLOW+"(6)"+Graphics.ANSI_BLUE);
        System.out.print("  13");
        System.out.print("  14");
        System.out.print("  15-" +Graphics.ANSI_YELLOW+"(9)"+Graphics.ANSI_RESET);
        System.out.print(Graphics.ANSI_RED+"  16"+Graphics.ANSI_RESET);
        System.out.print("  18-" +Graphics.ANSI_YELLOW+"(12)"+Graphics.ANSI_RESET);
        System.out.print(Graphics.ANSI_BLUE+"  19");
        System.out.print("  20");
        System.out.print("  21-" +Graphics.ANSI_YELLOW+"(16)"+Graphics.ANSI_RESET);
        System.out.print(Graphics.ANSI_BLUE+"  22");
        System.out.print("  23");
        System.out.print(Graphics.ANSI_RED+"  24-"+Graphics.ANSI_YELLOW+"(20)\n"+Graphics.ANSI_RESET);
    }

    public static String getMarble(Marble marble)
    {
        String result = "";
        switch (marble){
            case YELLOW -> result = ANSI_YELLOW+"●"+ANSI_RESET;
            case PURPLE -> result= ANSI_PURPLE+"●"+ANSI_RESET;
            case BLUE-> result= ANSI_BLUE+"●"+ANSI_RESET;
            case RED -> result= ANSI_RED+"●"+ANSI_RESET;
            case GREY -> result= ANSI_GREY+"●"+ANSI_RESET;
            case WHITE -> result= ANSI_WHITE+"●"+ANSI_RESET;

        }
        return result;
    }

    public static String getAbility(SimpleAbility.Type type, Resource res) {
        switch (type){
            case CARDDISCOUNT -> {
                return "-1 "+ getResource(res);
            }
            case EXTRADEPOT -> {
                return "Extra depot: " + getResource(res);
            }
            case WHITEMARBLE -> {
                return ANSI_WHITE+"   ● = "+getResource(res);
            }
            case EXTRAPRODUCTION -> {
                return "1"+getResource(res)+" } "+"1 ? 1"+getResource(Resource.FAITH);
            }
        }
        return null;
    }
}