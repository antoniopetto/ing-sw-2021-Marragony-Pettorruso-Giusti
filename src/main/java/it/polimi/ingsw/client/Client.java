package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.View;

public class Client {

    public static void main(String[] args) {

        int choice = CLIView.askChoice("Please select how to play:", "CLI", "GUI");

        if(choice == 1){
          View  view = new CLIView();
            view.startSetting();
        }
        else{
              GUIView view =new GUIView();
                GUIView.main(args);
        }
    }
}