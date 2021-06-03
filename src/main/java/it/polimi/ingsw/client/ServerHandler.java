package it.polimi.ingsw.client;


import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.update.UpdateMsg;
import it.polimi.ingsw.messages.toview.ViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private final Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private SimpleModel model;
    private final View view;
    private boolean running;

    public ServerHandler(Socket socket, View view) {
        serverSocket = socket;
        this.view = view;
    }

    @Override
    public void run() {
        running = true;
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            input = new ObjectInputStream(serverSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error getting the streams");
            return;
        }
        while(running) {
            try {
                Object message = input.readObject();
                System.out.println(message);
                if(message instanceof UpdateMsg) {
                    UpdateMsg updateMsg = (UpdateMsg)message;
                    updateMsg.execute(model);
                }
                else {
                    ViewMsg viewMsg = (ViewMsg)message;
                    viewMsg.changeView(view, this);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Connection dropped.");
                view.endGame();
                running = false;
            }
        }
    }

    public void writeObject(Object o) throws IOException{
        output.writeObject(o);
    }

    public void setModel(SimpleModel model){
        this.model = model;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
