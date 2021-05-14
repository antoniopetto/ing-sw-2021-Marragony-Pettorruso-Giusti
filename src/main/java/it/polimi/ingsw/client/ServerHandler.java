package it.polimi.ingsw.client;


import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.update.UpdateMsg;
import it.polimi.ingsw.messages.view.ViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private final Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final SimpleGame model;
    private final View view;

    public ServerHandler(Socket socket, View view, SimpleGame game) {
        serverSocket=socket;
        this.view=view;
        model = game;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            input = new ObjectInputStream(serverSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error getting the streams");
            return;
        }
        while(true) {
            try {
                Object message = input.readObject();
                if(message instanceof UpdateMsg) {
                    UpdateMsg updateMsg = (UpdateMsg)message;
                    updateMsg.execute(model);
                }
                else {
                    ViewMsg viewMsg = (ViewMsg)message;
                    viewMsg.changeView(view, this);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void writeObject(Object o) throws IOException{
        output.writeObject(o);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }
}
