package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.GUI.InitializeGame;
import it.polimi.ingsw.client.view.View;
import javafx.application.Application;

import java.util.Scanner;

public class Client {



    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean valid = false;
        System.out.println("Please select how to play:");
        System.out.println("1) CLI");
        System.out.println("2) GUI");
        System.out.print(">");
        int choice=0;
        while(!valid){
            try{
                choice = input.nextInt();
                if(choice!=1&&choice!=2) throw new Exception();
                valid=true;
            }catch (Exception e )
            {
                System.out.println("Invalid input. Try again");
            }
        }
        View view;
        if(choice==1) {
            view = new CLIView();
        }
        else {
            view = new GUIView();
        }
        view.startSetting();
    }

}
