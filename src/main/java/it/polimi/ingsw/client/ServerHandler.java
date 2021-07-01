package it.polimi.ingsw.client;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.shared.messages.update.UpdateMsg;
import it.polimi.ingsw.shared.messages.toview.ToViewMsg;
import org.apache.logging.log4j.ThreadContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
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

        String pid = ManagementFactory.getRuntimeMXBean().getName();
        ThreadContext.put("PID", pid.substring(0, (pid.contains("@")) ? pid.indexOf("@") : pid.length()));
        Client.logger.info("Server handler started");

        Thread workingThread = null;
        running = true;
        try {
            output = new ObjectOutputStream(serverSocket.getOutputStream());
            input = new ObjectInputStream(serverSocket.getInputStream());
        } catch (IOException e) {
            Client.logger.warn("Connection dropped with server [" + serverSocket.getInetAddress() + "]");
            running = false;
        }
        while(running) {
            try {
                Object message = input.readUnshared();
                Client.logger.debug(message);
                if (workingThread != null)
                    workingThread.join();
                if(message instanceof UpdateMsg) {
                    UpdateMsg updateMsg = (UpdateMsg)message;
                    workingThread = new Thread(() -> updateMsg.execute(model));
                }
                else {
                    ToViewMsg toViewMsg = (ToViewMsg)message;
                    workingThread = new Thread(() -> toViewMsg.changeView(view, this));
                }
                workingThread.start();
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                closeServerHandler();
            }
        }
    }

    public void closeServerHandler(){
        Client.logger.warn("Connection dropped with server [" + serverSocket.getInetAddress() + "]");
        view.endGame();
        running = false;
    }

    public void writeObject(Object o){
        try{
            output.writeObject(o);
        }
        catch (IOException e){
            closeServerHandler();
        }
    }

    public void setModel(SimpleModel model){
        this.model = model;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}