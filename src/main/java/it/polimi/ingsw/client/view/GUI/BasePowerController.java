package it.polimi.ingsw.client.view.GUI;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.exceptions.UncheckedInterruptedException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the base power stage.
 */
public class BasePowerController  implements Initializable {
    @FXML
    private Group inputResources;
    @FXML
    private Group outputResources;
    @FXML
    private Circle circleIn1;
    @FXML
    private Circle circleIn2;
    @FXML
    private Circle circleOut;
    @FXML
    private ImageView resIn1;
    @FXML
    private ImageView resIn2;
    @FXML
    private Text title;
    private Resource choice=null;

    public void showInput(boolean show){GUISupport.setVisible(show, inputResources);}

    /**
     * It shows the first resource selected as input
     * @param resource is the resource selected
     */
    public void setResIn1(Resource resource){
        GUISupport.setVisible(false, circleIn1);
        String path = "/images/res-marbles/" +GUISupport.returnPath(resource.name());
        resIn1.setImage(new Image(path));
        GUISupport.setVisible(true, resIn1);
        GUISupport.setVisible(true, circleIn2);
    }

    /**
     * It shows the second resource selected as input
     * @param resource is the resource selected
     */
    public void setResIn2(Resource resource){
        GUISupport.setVisible(false, circleIn2);
        GUISupport.setVisible(false, inputResources);
        GUISupport.setVisible(true, outputResources);
        GUISupport.setVisible(true, circleOut);
        String path = "/images/res-marbles/" +GUISupport.returnPath(resource.name());
        resIn2.setImage(new Image(path));
        GUISupport.setVisible(true, resIn2);
    }

    /**
     * @return the resource selected.
     */
    public synchronized Resource getChoice() {
        while (choice == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }
        Resource tmpChoice =choice;
        choice=null;
        return tmpChoice;
    }

    public synchronized void setChoice(Resource choice) {
        this.choice = choice;
        notifyAll();
    }

    /**
     * It's the event handler of the click event on a resource, chosen as input/output resource.
     * @param mouseEvent is the click event
     */
    public void resSelected(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ImageView resource=(ImageView)mouseEvent.getSource();
        String url =resource.getImage().getUrl();
        if(url.contains("coin.png")) setChoice(Resource.COIN);
        else if(url.contains("shield.png")) setChoice(Resource.SHIELD);
        else if(url.contains("stone.png")) setChoice(Resource.STONE);
        else setChoice(Resource.SERVANT);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUISupport.setFont(25, title);
    }
}
