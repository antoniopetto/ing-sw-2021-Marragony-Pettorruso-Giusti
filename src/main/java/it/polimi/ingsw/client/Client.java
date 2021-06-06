package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.lang.management.ManagementFactory;
import java.util.Random;

public class Client {

    public static final Logger logger = LogManager.getLogger(Client.class);

    public static void main(String[] args) {

        String pid = ManagementFactory.getRuntimeMXBean().getName();
        ThreadContext.put("PID", pid.substring(0, (pid.contains("@")) ? pid.indexOf("@") : pid.length()));
        logger.debug(System.lineSeparator());
        logger.debug("Starting client");

        int choice = CLIView.askChoice("Please select how to play:", "CLI", "GUI");
        View view = ((choice == 1) ? new CLIView() : new GUIView());
        if (choice == 1)
            view.startConnection();
        else
            GUIView.main(args);
    }
}