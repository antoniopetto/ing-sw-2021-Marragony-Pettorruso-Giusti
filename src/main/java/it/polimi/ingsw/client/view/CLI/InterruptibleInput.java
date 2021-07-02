package it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.UncheckedInterruptedException;

import java.io.*;
import java.util.concurrent.Callable;

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
