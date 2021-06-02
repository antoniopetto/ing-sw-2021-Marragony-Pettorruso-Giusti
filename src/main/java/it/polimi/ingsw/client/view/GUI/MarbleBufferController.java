package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.view.GUI.GUISupport.returnPath;

public class MarbleBufferController implements Initializable {

    private Marble marble = null;
    private DepotName depotName = null;
    private SimpleModel simpleModel = null;
    private Resource resource = null;
    private String resourceString = null;
    @FXML
    private ImageView res1;
    @FXML
    private ImageView res2;
    @FXML
    private ImageView res3;
    @FXML
    private ImageView res4;
    @FXML
    private ImageView marble1;
    @FXML
    private ImageView marble2;
    @FXML
    private ImageView marble3;
    @FXML
    private ImageView marble4;
    @FXML
    private ImageView extraCard1;
    @FXML
    private ImageView extraCard2;
    @FXML
    private ImageView wareHouse;
    @FXML
    private ImageView resourceHigh;
    @FXML
    private ImageView resourceSXMed;
    @FXML
    private ImageView resourceDXMed;
    @FXML
    private ImageView resourceCLow;
    @FXML
    private ImageView resourceSXLow;
    @FXML
    private ImageView resourceDXLow;
    @FXML
    private Button highDepot;
    @FXML
    private Button mediumDepot;
    @FXML
    private Button lowDepot;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void insertResource(ActionEvent actionEvent) {
        actionEvent.consume();

        Platform.runLater(() -> {
            Button button = (Button) actionEvent.getSource();
            switch (button.getId()) {
                case "highDepot" -> setDepotName(DepotName.HIGH);
                case "mediumDepot" -> setDepotName(DepotName.MEDIUM);
                case "lowDepot" -> setDepotName(DepotName.LOW);
            }
        });

    }


    @FXML
    public void selectMarble(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(() -> {
            ImageView marble = (ImageView) mouseEvent.getSource();
            String path = marble.getImage().getUrl();

            if (path.contains("black-marble.png")) setMarble(Marble.GREY);
            if (path.contains("blue-marble.png")) setMarble(Marble.BLUE);
            if (path.contains("purple-marble.png")) setMarble(Marble.PURPLE);
            if (path.contains("yellow-marble.png")) setMarble(Marble.YELLOW);
            if (path.contains("white-marble.png")) setMarble(Marble.WHITE);

        });
    }

    public void manageButton(boolean disable) {
        lowDepot.setDisable(disable);
        mediumDepot.setDisable(disable);
        highDepot.setDisable(disable);

    }

    public void show(boolean resources, boolean depot, boolean marble) {
        GUISupport.setVisible(resources, res1, res2, res3, res4);
        GUISupport.setVisible(marble, marble1, marble2, marble3, marble4);
        if (depot) setDepotResources();
        wareHouse.setVisible(depot);
        if (depot) activeExtraDepot();
    }

    private void setDepotResources() {
        int quantity;

        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.HIGH));
        GUISupport.settingImageView(quantity, resourceHigh);

        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.MEDIUM));
        GUISupport.settingImageView(quantity, resourceSXMed, resourceDXMed);


        quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepots().get(DepotName.LOW));
        GUISupport.settingImageView(quantity, resourceSXLow, resourceCLow, resourceDXLow);

//TODO: print resources in ExtraDepot
    }


    private void activeExtraDepot(){
        int counter = 0;
        for(SimpleLeaderCard simpleLeaderCard : simpleModel.getThisPlayer().getLeaderCards()){
            if(simpleLeaderCard.isActive()){
                if(simpleLeaderCard.getAbility().equals(SimpleLeaderCard.Ability.EXTRADEPOT)) {
                    if (counter == 0) {
                        extraCard1.setImage(new Image("/cards/leader/Leader-" + simpleLeaderCard.getId() + ".jpg"));
                        extraCard1.setVisible(true);
                    } else {
                        extraCard2.setImage(new Image("/cards/leader/Leader-" + simpleLeaderCard.getId() + ".jpg"));
                        extraCard2.setVisible(true);
                    }
                    counter++;
                }
            }
        }
    }


    @FXML
    public void selectResource(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(() -> {
            ImageView imageView = (ImageView) mouseEvent.getSource();
            switch (imageView.getId()){
                case "res1" -> setResource(Resource.SHIELD);
                case "res2" -> setResource(Resource.COIN);
                case "res3" -> setResource(Resource.SERVANT);
                case "res4" -> setResource(Resource.STONE);
            }
        });
    }

    public void setMarble(){
        int bufferSize = simpleModel.getMarbleBuffer().size();
        marbleCast(bufferSize, marble1, marble2, marble3, marble4);
    }

    private void marbleCast(int quantity, ImageView... marbleBuffer){
        for(int i = 0; i < quantity; i++){
            marbleBuffer[i].setImage(new Image("/res-marbles/" + GUISupport.getMarblePath(simpleModel.getMarbleBuffer().get(i).toString())));
            marbleBuffer[i].setVisible(true);
            marbleBuffer[i].setDisable(false);
        }
        if(quantity < marbleBuffer.length){
            for(int i = quantity; i < marbleBuffer.length; i++){
                marbleBuffer[i].setVisible(false);
            }
        }

    }

    public synchronized Resource getResource() {
        while (resource == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resource;
    }

    public synchronized void setResource(Resource resource) {
        this.resource = resource;
        notifyAll();
    }

    private synchronized void setMarble(Marble marble) {
        this.marble = marble;
        notifyAll();
    }

    public synchronized Marble getMarble() {
        while (marble == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return marble;
    }

    private synchronized void setDepotName(DepotName depotName) {
        this.depotName = depotName;
        notifyAll();
    }

    public synchronized DepotName getDepotName() {
        while (depotName == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return depotName;
    }

    public void setSimpleModel(SimpleModel simpleModel) {
        this.simpleModel = simpleModel;
    }

    @FXML
    public void insertAResource(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(() ->{
            ImageView card = (ImageView) mouseEvent.getSource();
            switch (card.getId()) {
                case "extraCard1" -> setDepotName(DepotName.FIRST_EXTRA);
                case "extraCard2" -> setDepotName(DepotName.SECOND_EXTRA);
            }
        });

    }

    @FXML
    public void switchDepot(MouseEvent mouseEvent) {
    }
}
