package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.CLI.Graphics;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.GUI.InitializeGame;
import it.polimi.ingsw.client.view.View;
import javafx.application.Application;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        int choice = CLIView.askChoice("Please select how to play:", "CLI", "GUI");
        View view = ((choice == 1) ? new CLIView() : new GUIView());
        view.startSetting();
    }
}