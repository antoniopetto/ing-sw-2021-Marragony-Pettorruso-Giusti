package it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.shared.exceptions.UncheckedInterruptedException;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * The InterruptibleInput is a class that can read a line from System.in, while still
 * being able to respond to interrupts. It's needed to interrupt the execution of a message
 * that is waiting for the input of the uses.
 */
public class InterruptibleInput implements Callable<String> {

    public String call(){

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        try {
            while (!br.ready()) {
                Thread.sleep(200);
            }
            input = br.readLine();
        }
        catch (IOException | InterruptedException e){
            throw new UncheckedInterruptedException();
        }
        return input;
    }
}
