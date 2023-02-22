package ui.screens.homestorage;

import game.DungeonLoader;
import game.demiurge.Demiurge;
import game.demiurge.DemiurgeContainerManager;
import game.object.Item;
import game.objectContainer.Chest;
import game.objectContainer.JewelryBag;
import game.objectContainer.Wearables;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerInvalidExchangeException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
            chest = (Chest) demiurge.getHome().getContainer();
            wearables = (Wearables) demiurge.getWizard().getWearables();
            bag = (JewelryBag) demiurge.getWizard().getJewelryBag();
            manager = new DemiurgeContainerManager(wearables, bag, chest);

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
                manager.exchangeItem(wearables, wearableItemIndex, chest, chestItemIndex);
                showJewelryBagList();
                showWearableList();
                this.getPrincipalController().setWearables();
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
                manager.exchangeItem(wearables, wearableItemIndex, bag, bagItemIndex);
                showChestList();
                showJewelryBagList();
                this.getPrincipalController().setWearables();
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
                int bagItemIndex = bagList.indexOf(jewel);
                int chestItemIndex = chestList.indexOf(chestItem);
                manager.exchangeItem(bag, bagItemIndex, wearables, chestItemIndex);
                showChestList();
                showWearableList();
            } catch (ContainerInvalidExchangeException e) {
                this.getPrincipalController().showErrorAlert("Only jewels are allowed");
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
            showWearableList();
            this.getPrincipalController().setWearables();
        }
    }

    public void removeFromJewelryBag() {
        Item item = listViewJewelryBag.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.getPrincipalController().showErrorAlert("Select an item from the jewelry bag");
        } else {
            int jewelItemIndex = bagList.indexOf(item);
            bag.remove(jewelItemIndex);
            showJewelryBagList();
        }
    }

    public void saveInChest() {
        Item wearable = listViewWearing.getSelectionModel().getSelectedItem();
        Item jewel = listViewJewelryBag.getSelectionModel().getSelectedItem();

        if (wearable != null && jewel != null) {
            this.getPrincipalController().showErrorAlert("Select only one object");
        } else if (wearable != null) {
            try {
                int wearableItemIndex = wearableList.indexOf(wearable);
                manager.addItem(wearables, wearableItemIndex, chest);
                this.getPrincipalController().setWearables();
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Chest is full");
            }
        } else if (jewel != null) {
            try {
                int jewelItemIndex = bagList.indexOf(jewel);
                manager.addItem(wearables, jewelItemIndex, chest);
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Chest is full");
            }
        }
        showWearableList();
        showChestList();
        showJewelryBagList();
    }

    public void saveInBag() {
        Item wearable = listViewWearing.getSelectionModel().getSelectedItem();
        Item chestItem = listViewChest.getSelectionModel().getSelectedItem();

        if (wearable != null && chestItem != null) {
            this.getPrincipalController().showErrorAlert("Select only one object");
        } else if (wearable != null) {
            try {
                int wearableItemIndex = wearableList.indexOf(wearable);
                manager.addItem(wearables, wearableItemIndex, bag);
                this.getPrincipalController().setWearables();
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Jewelry bag is full");
            }
        }else if (chestItem != null) {
            try {
                int chestItemIndex = bagList.indexOf(chestItem);
                manager.addItem(chest, chestItemIndex, bag);
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Jewelry bag is full");
            }
        }
        showWearableList();
        showChestList();
        showJewelryBagList();
    }

    public void saveAsWearable() {
        Item bagItem = listViewJewelryBag.getSelectionModel().getSelectedItem();
        Item chestItem = listViewChest.getSelectionModel().getSelectedItem();

        if (bagItem != null && chestItem != null) {
            this.getPrincipalController().showErrorAlert("Select only one object");
        } else if (bagItem != null) {
            try {
                int bagItemIndex = bagList.indexOf(bagItem);
                manager.addItem(wearables, bagItemIndex, wearables);
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Wearable is full");
            }
        }else if (chestItem != null) {
            try {
                int chestItemIndex = bagList.indexOf(chestItem);
                manager.addItem(chest, chestItemIndex, wearables);
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("This item can't be saved in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("Wearable is full");
            }
        }
        showWearableList();
        showChestList();
        showJewelryBagList();
        this.getPrincipalController().setWearables();
    }

    public void clearSelection() {
        listViewChest.getSelectionModel().clearSelection();
        listViewJewelryBag.getSelectionModel().clearSelection();
        listViewWearing.getSelectionModel().clearSelection();
    }
}
