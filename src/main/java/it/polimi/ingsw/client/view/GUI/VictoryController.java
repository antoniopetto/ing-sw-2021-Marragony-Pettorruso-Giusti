package it.polimi.ingsw.client.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

public class VictoryController implements Initializable {

    @FXML
    private Text title;
    @FXML
    private Group leaderboard;

    private boolean closeWindow = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setScene(Boolean win, Map<String, Integer> leaderboard){
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
                text.setText(count+") "+username+" - "+leaderboard.get(username)+" pts");
                count++;
            }
            else GUISupport.setVisible(false, text);
        }
    }

    public synchronized boolean isCloseWindow() {
        while(!closeWindow){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return closeWindow;
    }

    private synchronized void setCloseWindow(boolean close){
        closeWindow = close;
        notifyAll();
    }

    @FXML
    public void close(ActionEvent actionEvent){
        actionEvent.consume();
        setCloseWindow(true);

    }
}
