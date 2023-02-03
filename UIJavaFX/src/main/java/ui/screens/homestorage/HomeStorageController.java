package ui.screens.homestorage;

import game.demiurge.Demiurge;
import game.demiurge.DemiurgeContainerManager;
import game.object.Item;
import game.objectContainer.Chest;
import game.objectContainer.JewelryBag;
import game.objectContainer.Wearables;
import game.objectContainer.exceptions.ContainerInvalidExchangeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeStorageController extends BaseScreenController implements Initializable {
    private Demiurge demiurge;
    private Chest chest;
    private Wearables wearables;
    private JewelryBag bag;
    private DemiurgeContainerManager manager;

    @FXML
    private Pane removeJewelryBagBtn;
    @FXML
    private ScrollPane actionLogScrollPane;
    @FXML
    private ListView<Item> listViewChest;
    @FXML
    private ListView<Item> listViewWearing;
    @FXML
    private ListView<Item> listViewJewelryBag;
    @FXML
    private ImageView exchangeChestWearingImg;
    @FXML
    private ImageView exchangeJewelryWearingImg;
    @FXML
    private ImageView exchangeJewelryChestImg;
    @FXML
    private Button closeChestBtn;
    @FXML
    private Button removeChestItemBtn;
    @FXML
    private Button removeWearingItemBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exchangeChestWearingImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));
        exchangeJewelryWearingImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));
        exchangeJewelryChestImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));
    }

    @Override
    public void principalCargado() {
        demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            manager = demiurge.getContainerManager();
            chest = (Chest) demiurge.getHome().getContainer();
            wearables = (Wearables) demiurge.getWizard().getWearables();
            bag = (JewelryBag) demiurge.getWizard().getJewelryBag();
        }
    }

    @FXML
    private void exchangeChestWearing(MouseEvent mouseEvent) {
        Item chestItem = listViewChest.getSelectionModel().getSelectedItem();
        Item wearingItem = listViewWearing.getSelectionModel().getSelectedItem();
        if (chestItem == null || wearingItem == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the chest\nand another from the wearables");
        } else {
            try {
                manager.exchangeItem(chest, 1, wearables, 1);
            } catch (ContainerInvalidExchangeException e) {
                this.getPrincipalController().showErrorAlert("These items can't be exchange");
            }

        }
    }

    @FXML
    private void exchangeJewelryWearing(MouseEvent mouseEvent) {
    }

    @FXML
    private void exchangeJewelryChest(MouseEvent mouseEvent) {
    }

    @FXML
    private void closeChest(ActionEvent actionEvent) {
        this.getPrincipalController().goHome();
    }
}
