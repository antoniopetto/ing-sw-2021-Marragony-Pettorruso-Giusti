package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.server.model.playerboard.Resource;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    private int choice=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public synchronized int getChoice(){
        while (choice == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int tmpChoice = choice;
        choice = 0;
        return tmpChoice;
    }

    public synchronized void setChoice(int choice){
        this.choice = choice;
        notifyAll();
    }

    public void actionSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Text source=(Text)mouseEvent.getSource();
        String id = source.getId();
        switch (id){
            case "exit" -> setChoice(1);
            case "newGame" -> setChoice(2);
            case "newServer"-> setChoice(3);
        }
    }
}
