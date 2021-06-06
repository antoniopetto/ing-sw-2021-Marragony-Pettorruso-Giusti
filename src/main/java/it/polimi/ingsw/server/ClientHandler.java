package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler {

    private final Socket clientSocket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public ClientHandler(Socket clientSocket) throws IOException {

        this.clientSocket = clientSocket;
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        input = new ObjectInputStream(clientSocket.getInputStream());

    }

    public void writeObject(Object o) throws IOException{
        output.writeObject(o);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public void closeConnection() {
        try {
            input.close();
            output.close();
            clientSocket.close();
        }
        catch (IOException e){
            Server.logger.warn("Error closing the socket");
        }
    }

    public InetAddress getIP()
    {
        return clientSocket.getInetAddress();
    }
}
