package it.polimi.ingsw.client.view.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    @FXML
    private ImageView image;
    @FXML
    private VBox pane;
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.fitHeightProperty().bind(anchorPane.heightProperty());
        image.fitWidthProperty().bind(anchorPane.widthProperty());
    }
}
