package it.polimi.ingsw.client;

import it.polimi.ingsw.client.views.CLI.CLISettingView;
import it.polimi.ingsw.client.views.SettingView;
import it.polimi.ingsw.client.views.GUI.GUISettingView;

import java.io.IOException;
import java.net.Socket;
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
        new Thread(settingView).start();
    }

}
