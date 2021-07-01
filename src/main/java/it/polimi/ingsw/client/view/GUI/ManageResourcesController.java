package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;



/**
 * this controller allows to choose what to do with the Marbles contain in MarbleBuffer :
 * - Insert a Marble ( so its associated Resource ) in a Depot
 * - Discard a Marble
 * - Switch / Move Resources between Depots
 */
public class ManageResourcesController {

    private int choice = 0;

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
