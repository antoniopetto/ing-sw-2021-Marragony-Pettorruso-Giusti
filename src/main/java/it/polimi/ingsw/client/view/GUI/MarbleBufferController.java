package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.*;
import it.polimi.ingsw.shared.exceptions.UncheckedInterruptedException;
import it.polimi.ingsw.shared.DepotName;
import it.polimi.ingsw.shared.Resource;
import it.polimi.ingsw.shared.Marble;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class MarbleBufferController {

    private Marble marble = null;
    private SimpleModel simpleModel = null;
    private Resource resource = null;
    private DepotName depot = null;

    @FXML
    private ImageView res1;
    @FXML
    private ImageView res2;
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
    private ImageView resourceSxExtra1;
    @FXML
    private ImageView resourceDxExtra1;
    @FXML
    private ImageView resourceSxExtra2;
    @FXML
    private ImageView resourceDxExtra2;
    @FXML
    private Button highDepot;
    @FXML
    private Button mediumDepot;
    @FXML
    private Button lowDepot;
    @FXML
    private ImageView switchHigh;
    @FXML
    private ImageView switchMed;
    @FXML
    private ImageView switchLow;
    @FXML
    private ImageView switchExtra1;
    @FXML
    private ImageView switchExtra2;
    @FXML
    private Text titleLabel;
    @FXML
    private GridPane insertResource;


    /**
     * it inserts a previously chosen marble/resource in the clicked depot.
     *
     */
    @FXML
    public void insertResource(ActionEvent actionEvent) {
        actionEvent.consume();
        Platform.runLater(() -> {
            Button button = (Button) actionEvent.getSource();
            switch (button.getId()) {
                case "highDepot" -> setDepot(DepotName.HIGH);
                case "mediumDepot" -> setDepot(DepotName.MEDIUM);
                case "lowDepot" -> setDepot(DepotName.LOW);
            }
        });

    }

    /**
     * it returns the selected marble from the MarbleBuffer.
     *
     */
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


    /**
     * it disables or enables Depot buttons.
     *
     */
    public void manageButton(boolean disable) {
        lowDepot.setDisable(disable);
        mediumDepot.setDisable(disable);
        highDepot.setDisable(disable);
        extraCard2.setDisable(disable);
        extraCard1.setDisable(disable);
    }

    /**
     * it is used to show the warehouse/the marbleBuffer or whiteMarble resources
     * @param resources if it is true --> the resources associated to whiteMarble are shown
     * @param depot if it is true -->the Warehouse and its resources is shown
     * @param marble if it is true --> the MarbleBuffer is shown
     */
    public void show(boolean resources, boolean depot, boolean marble) {
        GUISupport.setVisible(marble, marble1, marble2, marble3, marble4);
        GUISupport.setFont(22, titleLabel);
        insertResource.setVisible(marble);
        if(resources){
            insertResource.setVisible(true);

            if(simpleModel.getThisPlayer().getLeaderCards().get(0).getAbility().getType().equals(SimpleAbility.Type.WHITEMARBLE)
              && simpleModel.getThisPlayer().getLeaderCards().get(0).isActive()){
                GUISupport.settingImageView(res1, simpleModel.getThisPlayer().getLeaderCards().get(0).getAbility().getResource().toString());
            }
            if(simpleModel.getThisPlayer().getLeaderCards().get(1).getAbility().getType().equals(SimpleAbility.Type.WHITEMARBLE)
                    && simpleModel.getThisPlayer().getLeaderCards().get(1).isActive()){
                GUISupport.settingImageView(res2, simpleModel.getThisPlayer().getLeaderCards().get(1).getAbility().getResource().toString());
            }
        }

        if (depot) setDepotResources();
            else GUISupport.setVisible(false, resourceHigh, resourceSXMed, resourceDXMed, resourceSXLow, resourceCLow, resourceDXLow);
        wareHouse.setVisible(depot);
        if (depot) activeExtraDepot();
    }

    /**
     * it inserts the resources in its own normal depot.
     */
    private void setDepotResources() {
        GUISupport.setVisible(false, resourceHigh, resourceSXMed, resourceDXMed, resourceSXLow, resourceCLow, resourceDXLow);
        int quantity = GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.HIGH));
        GUISupport.settingImageView(quantity, resourceHigh);

        quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.MEDIUM));
        GUISupport.settingImageView(quantity, resourceSXMed, resourceDXMed);

        quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.LOW));
        GUISupport.settingImageView(quantity, resourceSXLow, resourceCLow, resourceDXLow);

    }


    /**
     * it shows active leaderCards with extraDepot ability.
     * it inserts the resources in its own extra depot.
     */
    private void activeExtraDepot(){

        for(SimpleLeaderCard card : simpleModel.getThisPlayer().getLeaderCards()){
            if(card.isActive() && card.getAbility().getType().equals(SimpleAbility.Type.EXTRADEPOT)) {
                int quantity;
                if(card.getAbility().getResource().equals(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA).getConstraint())){
                 extraCard1.setImage(new Image("/images/leader/Leader-" + card.getId() + ".jpg"));
                 extraCard1.setVisible(true);
                 quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.FIRST_EXTRA));
                 GUISupport.settingImageView(quantity, resourceSxExtra1, resourceDxExtra1);
                } else {
                    extraCard2.setImage(new Image("/images/leader/Leader-" + card.getId() + ".jpg"));
                    extraCard2.setVisible(true);
                    quantity =  GUISupport.quantityOfResources(simpleModel.getThisPlayer().getWarehouse().getDepot(DepotName.SECOND_EXTRA));
                    GUISupport.settingImageView(quantity, resourceSxExtra2, resourceDxExtra2);
                }
            }
        }
    }

    /**
     * it sets the selected resource associated to whiteMarble.
     *
     */
    @FXML
    public void selectResource(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(() -> {
            ImageView imageView = (ImageView) mouseEvent.getSource();
            switch (imageView.getId()){
                case "res1" -> setResource(simpleModel.getThisPlayer().getLeaderCards().get(0).getAbility().getResource());
                case "res2" -> setResource(simpleModel.getThisPlayer().getLeaderCards().get(1).getAbility().getResource());
            }
        });
    }

    public void setMarble(){
        int bufferSize = simpleModel.getMarbleBuffer().size();
        marbleCast(bufferSize, marble1, marble2, marble3, marble4);
    }

    /**
     * it allocates the right marble taken from the MarbleBuffer.
     * @param quantity: number of resources in MarbleBuffer.
     * @param marbleBuffer: all Marbles in MarbleBuffer.
     */
    private void marbleCast(int quantity, ImageView... marbleBuffer){
        for(int i = 0; i < quantity; i++){
            marbleBuffer[i].setImage(new Image("/images/res-marbles/" + GUISupport.getMarblePath(simpleModel.getMarbleBuffer().get(i).toString())));
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
                throw new UncheckedInterruptedException();
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
                throw new UncheckedInterruptedException();
            }
        }
        return marble;
    }

    public void setSimpleModel(SimpleModel simpleModel) {
        this.simpleModel = simpleModel;
    }


    /**
     * it inserts a previously selected resources in an extra depot.
     *
     */
    @FXML
    public void insertAResource(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Platform.runLater(() ->{
            ImageView card = (ImageView) mouseEvent.getSource();
            switch (card.getId()) {
                case "extraCard1" -> setDepot(DepotName.FIRST_EXTRA);
                case "extraCard2" -> setDepot(DepotName.SECOND_EXTRA);
            }
        });

    }

    /**
     *
     * @param visible: if it is true, the buttons for switch Resources are visible.
     */
    public void setSwitchButton(boolean visible){
        GUISupport.setVisible(visible, switchLow, switchMed, switchHigh);
        if(extraCard1.isVisible()) switchExtra1.setVisible(visible);
        if(extraCard2.isVisible()) switchExtra2.setVisible(visible);
    }

    /**
     * when a switchButton is clicked, this method sends one of two depotNames used to the switch.
     *
     */
    @FXML
    public void switchDepot(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ImageView button = (ImageView) mouseEvent.getSource();
        System.out.println("button:" + button.getId());

        switch (button.getId()){
            case "switchLow" ->{
                setDepot(DepotName.LOW);
                switchLow.setVisible(false);
            }
            case "switchMed" ->{
                setDepot(DepotName.MEDIUM);
                switchMed.setVisible(false);
            }
            case "switchHigh" ->{
                setDepot(DepotName.HIGH);
                switchHigh.setVisible(false);
            }
            case "switchExtra1" ->{
                setDepot(DepotName.FIRST_EXTRA);
                switchExtra1.setVisible(false);
            }
            case "switchExtra2" ->{
                setDepot(DepotName.SECOND_EXTRA);
                switchExtra2.setVisible(false);
            }

        }
    }

    private synchronized void setDepot(DepotName depot) {
        this.depot = depot;
        notifyAll();
    }


    public synchronized DepotName getDepot() {
        while (depot == null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }
        DepotName tmpDepot = depot;
        depot = null;
        return tmpDepot;
    }

    public void changeTitle(String text)
    {
        titleLabel.setText(text);
    }

}
