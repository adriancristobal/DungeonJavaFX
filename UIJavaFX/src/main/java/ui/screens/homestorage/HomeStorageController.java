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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeStorageController extends BaseScreenController implements Initializable {
    private Chest chest;
    private Wearables wearables;
    private JewelryBag bag;
    private DemiurgeContainerManager manager;
    private List<Item> chestList;
    private List<Item> wearableList;
    private List<Item> bagList;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exchangeChestWearingImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));
        exchangeJewelryWearingImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));
        exchangeJewelryChestImg.setImage(new Image(getClass().getResource("/images/transfer.png").toExternalForm()));

        chestList = new ArrayList<>();
        wearableList = new ArrayList<>();
        bagList = new ArrayList<>();
    }

    @Override
    public void principalCargado() {
        Demiurge demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            manager = demiurge.getContainerManager();
            chest = (Chest) demiurge.getHome().getContainer();
            wearables = (Wearables) demiurge.getWizard().getWearables();
            bag = (JewelryBag) demiurge.getWizard().getJewelryBag();

            chest.iterator().forEachRemaining(item -> chestList.add((Item) item));
            listViewChest.getItems().addAll(chestList);

            wearables.iterator().forEachRemaining(item -> wearableList.add((Item) item));
            listViewWearing.getItems().addAll(wearableList);

            bag.iterator().forEachRemaining(item -> bagList.add((Item) item));
            listViewJewelryBag.getItems().addAll(bagList);
        } else {
            this.getPrincipalController().showErrorAlert("Can't find Dungeon");
        }
    }

    private void showChestList() {
        chestList = new ArrayList<>();
        chest.iterator().forEachRemaining(item -> chestList.add((Item) item));
        listViewChest.getItems().clear();
        listViewChest.getItems().addAll(chestList);
    }

    private void showWearableList() {
        wearableList = new ArrayList<>();
        wearables.iterator().forEachRemaining(item -> wearableList.add((Item) item));
        listViewWearing.getItems().clear();
        listViewWearing.getItems().addAll(wearableList);
    }

    private void showJewelryBagList() {
        bagList = new ArrayList<>();
        bag.iterator().forEachRemaining(item -> bagList.add((Item) item));
        listViewJewelryBag.getItems().clear();
        listViewJewelryBag.getItems().addAll(bagList);
    }

    @FXML
    private void exchangeChestWearing() {
        Item chestItem = listViewChest.getSelectionModel().getSelectedItem();
        Item wearingItem = listViewWearing.getSelectionModel().getSelectedItem();
        if (chestItem == null || wearingItem == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the chest\nand another from the wearables");
        } else {
            try {
                int chestItemIndex = chestList.indexOf(chestItem);
                int wearableItemIndex = wearableList.indexOf(wearingItem);
                manager.exchangeItem(chest, chestItemIndex, wearables, wearableItemIndex);
                showJewelryBagList();
                showWearableList();
            } catch (ContainerInvalidExchangeException e) {
                this.getPrincipalController().showErrorAlert("These items can't be exchange");
            }

        }
    }

    @FXML
    private void exchangeJewelryWearing() {
        Item jewel = listViewJewelryBag.getSelectionModel().getSelectedItem();
        Item wearingItem = listViewWearing.getSelectionModel().getSelectedItem();
        if (jewel == null || wearingItem == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the jewelry bag\nand another from the wearables");
        } else {
            try {
                int bagItemIndex = chestList.indexOf(jewel);
                int wearableItemIndex = wearableList.indexOf(wearingItem);
                manager.exchangeItem(bag, bagItemIndex, wearables, wearableItemIndex);
                showChestList();
                showJewelryBagList();
            } catch (ContainerInvalidExchangeException e) {
                this.getPrincipalController().showErrorAlert("These items can't be exchange");
            }
        }
    }

    @FXML
    private void exchangeJewelryChest() {
        Item jewel = listViewJewelryBag.getSelectionModel().getSelectedItem();
        Item chestItem = listViewChest.getSelectionModel().getSelectedItem();
        if (jewel == null || chestItem == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the jewelry bag\nand another from the chest");
        } else {
            try {
                int bagItemIndex = chestList.indexOf(jewel);
                int chestItemIndex = chestList.indexOf(chestItem);
                manager.exchangeItem(bag, bagItemIndex, wearables, chestItemIndex);
                showChestList();
                showWearableList();
            } catch (ContainerInvalidExchangeException e) {
                this.getPrincipalController().showErrorAlert("These items can't be exchange");
            }
        }
    }

    @FXML
    private void closeChest() {
        this.getPrincipalController().goHome();
    }

    public void removeFromChest() {
        Item item = listViewChest.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the chest");
        } else {
            int chestItemIndex = chestList.indexOf(item);
            chest.remove(chestItemIndex);
            showChestList();
        }
    }

    public void removeFromWearables() {
        Item item = listViewWearing.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the wearable");
        } else {
            int wearableItemIndex = wearableList.indexOf(item);
            wearables.remove(wearableItemIndex);
            showChestList();
        }
    }

    public void removeFromJewelryBag() {
        Item item = listViewJewelryBag.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the jewelry bag");
        } else {
            int jewelItemIndex = bagList.indexOf(item);
            bag.remove(jewelItemIndex);
            showChestList();
        }
    }
}
