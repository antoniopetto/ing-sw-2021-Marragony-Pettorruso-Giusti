package it.polimi.ingsw.server;

import javax.imageio.event.IIOWriteProgressListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public ClientHandler(Socket clientSocket) throws IOException {

        this.clientSocket = clientSocket;
        input = new ObjectInputStream(clientSocket.getInputStream());
        output = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void writeObject(Object o) throws IOException{
        output.writeObject(o);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public void closeConnection() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
    }
}
