package it.polimi.ingsw.client;


import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.update.UpdateMsg;
import it.polimi.ingsw.shared.messages.view.ViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private final Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private SimpleGame model;
    private View view;

    public ServerHandler(Socket socket, View view) {
        serverSocket=socket;
        this.view=view;

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
        while(true)
        {
            try {

                Object message = input.readObject();
                if(message instanceof UpdateMsg)
                {
                    UpdateMsg updateMsg = (UpdateMsg)message;
                    updateMsg.execute(model);
                }
                else
                {
                    ViewMsg viewMsg = (ViewMsg)message;
                    viewMsg.changeView(view, this);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error Message");
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
