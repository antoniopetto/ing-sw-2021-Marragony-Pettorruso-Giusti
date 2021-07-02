package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.UncheckedInterruptedException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * this controller allows to choose what to do with the Marbles contain in MarbleBuffer :
 * - Insert a Marble ( so its associated Resource ) in a Depot
 * - Discard a Marble
 * - Switch / Move Resources between Depots
 */
public class ManageResourcesController implements Initializable {

    private int choice = 0;
    @FXML
    private Text title;
    @FXML
    private Label insertResource;
    @FXML
    private Label discardMarble;
    @FXML
    private Label switchResources;

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
                throw new UncheckedInterruptedException();
            }
        }
        return choice;
    }

    public synchronized void setChoice(int choice) {
        this.choice = choice;
        notifyAll();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUISupport.setFont(26, title);
        GUISupport.setFont(25, insertResource, switchResources, discardMarble);
    }
}
