package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.VirtualView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    /**
     * Static block needed to set a System property with the location of the base folder where we can save data from
     * the execution. In the client it's needed by the logger.
     */
    static {
        try{
            URI jarUri = VirtualView.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            URI baseDirUri = jarUri.getPath().endsWith("/") ? jarUri.resolve("..") : jarUri.resolve(".");
            System.setProperty("mor.base", baseDirUri.getPath());
        }
        catch (URISyntaxException e){
            System.out.println("Could not get baseDir");
        }
    }

    public static final Logger logger = LogManager.getLogger(Client.class);

    /**
     * After setting some logging parameter, we ask for the type of view and start it
     * @param args  Ignored arguments
     */
    public static void main(String[] args) {

        String pid = ManagementFactory.getRuntimeMXBean().getName();
        ThreadContext.put("PID", pid.substring(0, (pid.contains("@")) ? pid.indexOf("@") : pid.length()));
        logger.debug(System.lineSeparator());
        logger.debug("Starting client");

        int choice = CLIView.askChoice("Please select how to play:", "CLI", "GUI");
        View view = ((choice == 1) ? new CLIView() : new GUIView());
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            //TODO remove print
            System.out.println("Hello");
            if (view.getServerHandler() != null)
                view.getServerHandler().stopWorkingThread();
        }));
        view.startConnection();
    }
}