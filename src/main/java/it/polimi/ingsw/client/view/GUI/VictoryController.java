package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.shared.exceptions.UncheckedInterruptedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import java.util.Iterator;
import java.util.Map;

/**
 * This is the controller of the victory stage. It opens after the victory of a player.
 */
public class VictoryController {

    @FXML
    private Text title;
    @FXML
    private Group leaderboard;

    private boolean closeWindow = false;

    /**
     * It sets the scene with the name of the winner and the leaderboard
     * @param win tells if the player won in a single player game. In a multiplayer game it is null.
     * @param leaderboard is the final leaderboard.
     */
    public void setScene(Boolean win, Map<String, Integer> leaderboard){
        GUISupport.setFont(45, title);

        Iterator<String> iterator = leaderboard.keySet().iterator();
        int count =1;
        for (Node node: this.leaderboard.getChildren()) {
            Text text = (Text) node;
            if(iterator.hasNext()){
                String username = iterator.next();
                if(count==1){
                    if(win!=null){
                        if(!win) title.setText("You lost.");
                    }
                    else title.setText(username+" won!");
                }
                GUISupport.setFont(22, text);
                text.setText(count+") "+username+" - "+leaderboard.get(username)+" pts");
                count++;
            }
            else GUISupport.setVisible(false, text);
        }
    }

    /**
     * @return a boolean that tells if the close button has been pressed.
     */
    public synchronized boolean isCloseWindow() {
        while(!closeWindow){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }
        return true;
    }

    private synchronized void setCloseWindow(){
        closeWindow = true;
        notifyAll();
    }

    /**
     * Is the event handler of the click event on the close button.
     * @param actionEvent is the click event.
     */
    @FXML
    public void close(ActionEvent actionEvent){
        actionEvent.consume();
        setCloseWindow();

    }
}
