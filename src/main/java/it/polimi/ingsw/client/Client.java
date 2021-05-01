package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLISettingView;
import it.polimi.ingsw.client.view.SettingView;
import it.polimi.ingsw.client.view.GUI.GUISettingView;

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
        SettingView settingView;
        if(choice==1)
            settingView = new CLISettingView();
        else
            settingView = new GUISettingView();
        settingView.execute();
    }

}
