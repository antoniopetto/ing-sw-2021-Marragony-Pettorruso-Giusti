package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageResourcesController implements Initializable {

    private int choice = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void marbleAction(MouseEvent mouseEvent) {
        mouseEvent.consume();

        Label label = (Label) mouseEvent.getSource();
        switch (label.getId()){
            case "insertResource" ->setChoice(1);
            case "discardMarble" ->setChoice(2);
            case "switchResources" ->setChoice(3);
        }

    }


    public synchronized int getChoice() {
        while (choice == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return choice;
    }

    public synchronized void setChoice(int choice) {
        this.choice = choice;
        notifyAll();
    }
}
