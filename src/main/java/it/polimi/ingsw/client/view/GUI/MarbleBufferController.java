package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.SimpleLeaderCard;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.server.model.playerboard.Depot;
import it.polimi.ingsw.server.model.playerboard.DepotName;
import it.polimi.ingsw.server.model.playerboard.Resource;
import it.polimi.ingsw.server.model.shared.Marble;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

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
        setImageView(resources, res1, res2, res3, res4);
        setImageView(marble, marble1, marble2, marble3, marble4);
        if (depot) setDepotResources();
        setImageView(depot, wareHouse);
        if (depot) activeExtraDepot();
    }

    private void setDepotResources() {
        String path = "/res-marbles/";
        int quantity = 0;

        quantity = printResources(DepotName.HIGH);
        if (quantity > 0) {
            resourceHigh.setImage(new Image(path + resourceString));
            setImageView(true, resourceHigh);
        }

        quantity = printResources(DepotName.MEDIUM);
        if (quantity > 0) {
            resourceSXMed.setImage(new Image(path + resourceString));
            setImageView(true, resourceSXMed);
        }
        if (quantity > 1) {
            resourceDXMed.setImage(new Image(path + resourceString));
            setImageView(true, resourceDXMed);
        }


        quantity =printResources(DepotName.LOW);
            if(quantity >0)
            {
        resourceSXLow.setImage(new Image(path + resourceString));
        setImageView(true, resourceSXLow);
            }
            if(quantity >1)
            {
          resourceCLow.setImage(new Image(path + resourceString));
        resourceCLow.setVisible(true);
        resourceCLow.setDisable(false);
        }
            if(quantity >2)
            {
        resourceDXLow.setImage(new Image(path + resourceString));
        resourceDXLow.setVisible(true);
        resourceDXLow.setDisable(false);
    }

//TODO: print resources in ExtraDepot
    }

    private int printResources(DepotName depotName){

        Map<Resource, Integer> map =  simpleModel.getThisPlayer().getWarehouse().getDepots().get(depotName);
        if(map!=null) {
            int quantity = 1;
            for (Resource resource1 : map.keySet()) {
                quantity = map.get(resource1);
                resourceString = returnPath(resource1.toString());
            }
            return quantity;
        }else return 0;

    }

    private String returnPath(String resource){
        switch (resource) {
            case "COIN" -> {
                return "coin.png";
            }
            case "STONE" -> {
                return "stone.png";
            }
            case "SHIELD" -> {
                return "shield.png";
            }
            case "SERVANT" -> {
                return "servant";
            }
            default ->{return null;}
        }
    }

    private void activeExtraDepot(){
        int counter = 0;
        for(SimpleLeaderCard simpleLeaderCard : simpleModel.getThisPlayer().getLeaderCards()){
            if(simpleLeaderCard.isActive()){
                if(simpleLeaderCard.getAbility().equals(SimpleLeaderCard.Ability.EXTRADEPOT)) {
                    if (counter == 0) {
                        extraCard1.setImage(new Image("/cards/leader/Leader-" + simpleLeaderCard.getId() + ".jpg"));
                        extraCard1.setVisible(true);
                        extraCard1.setDisable(false);
                    } else {
                        extraCard2.setImage(new Image("/cards/leader/Leader-" + simpleLeaderCard.getId() + ".jpg"));
                        extraCard2.setDisable(false);
                        extraCard2.setVisible(true);
                    }
                    counter++;
                }
            }
        }
    }


    private void setImageView(boolean available, ImageView... object){
        for(ImageView imageView : object){
            imageView.setVisible(available);
            imageView.setDisable(!available);
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
}
