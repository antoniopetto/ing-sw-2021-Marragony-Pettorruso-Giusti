package it.polimi.ingsw.client;


import it.polimi.ingsw.client.simplemodel.SimpleGame;
import it.polimi.ingsw.client.views.View;
import it.polimi.ingsw.shared.messages.server.ServerMsg;
import it.polimi.ingsw.shared.messages.view.ViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private SimpleGame model;
    private View view;

    public ServerHandler(Socket socket, View view) throws IOException {
        serverSocket=socket;
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        this.view=view;
    }


    @Override
    public void run() {
        while(true)
        {
            try {
                Object message = input.readObject();
                if(ServerMsg.class.isInstance(message))
                {
                    ServerMsg updateMsg = (ServerMsg)message;
                    updateMsg.execute(model);
                }
                else
                {
                    ViewMsg viewMsg = (ViewMsg)message;
                    viewMsg.changeView(view, this);
                }

            } catch (IOException | ClassNotFoundException e) {
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
