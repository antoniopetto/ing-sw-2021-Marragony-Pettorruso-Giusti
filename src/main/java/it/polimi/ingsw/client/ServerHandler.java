package it.polimi.ingsw.client;


import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.shared.messages.ServerMsg;
import it.polimi.ingsw.shared.messages.TrackUpdateMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private SimpleModel model;

    public ServerHandler(Socket socket) throws IOException {
        serverSocket=socket;
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }


    @Override
    public void run() {
        while(true)
        {
            try {
                Object message = input.readObject();
                ServerMsg serverMsg = (ServerMsg)message;
                serverMsg.execute(model);
            } catch (IOException | ClassNotFoundException e) {
            }

        }
    }
}
