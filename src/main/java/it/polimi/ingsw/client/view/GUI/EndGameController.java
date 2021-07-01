package it.polimi.ingsw.client.view.GUI;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
/**
 * This class is the controller of the window shown at the end of a game. The player has 3 choices: to end the game, to start
 * a new game on the same server or to start a new game on a different server.
 */
public class EndGameController {
    private int choice=0;

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

    /**
     * This is the event handler of the click event on one of the possible choices.
     * @param mouseEvent is the click event on the Text.
     */
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
