package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.View;

public class Client {

    public static void main(String[] args) {

        int choice = CLIView.askChoice("Please select how to play:", "CLI", "GUI");
        View view = ((choice == 1) ? new CLIView() : new GUIView());
        if (choice == 1)
            view.startConnection();
        else
            GUIView.main(args);
    }
}