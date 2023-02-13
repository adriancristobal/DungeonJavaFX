package ui.screens.dungeon;

import game.actions.Attack;
import game.character.Wizard;
import game.character.exceptions.CharacterKilledException;
import game.demiurge.*;
import game.dungeon.Room;
import game.dungeon.Site;
import game.object.Item;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RoomNoMonsterDungeonController extends BaseScreenController implements Initializable {

    private int roomId;
    private Room room;

    private boolean hasMonster;
    private Demiurge demiurge;
    private Wizard wizard;
    @FXML
    private ImageView imgExit;
    @FXML
    private Label lblExit;
    @FXML
    private Pane battleMenuPane;
    @FXML
    private Button runBtn;
    @FXML
    private Button castSpellBtn;
    @FXML
    private Button physicalAttackBtn;
    @FXML
    private ComboBox spellComboBox;
    @FXML
    private ImageView imgCrystal;
    @FXML
    private ImageView imgWearable;
    @FXML
    private ImageView imgMonster;
    @FXML
    private Pane explorersMenuPane;
    @FXML
    private Button lookForItemsBtn;
    @FXML
    private ComboBox roomMoveComboBox;
    @FXML
    private Button moveThereBtn;
    @FXML
    private ListView<String> wizardItemsListView;
    @FXML
    private Button removeBtn;
    @FXML
    private ImageView exchangeItemRoomBagBtn;
    @FXML
    private Text crystalLabel;
    @FXML
    private Text werableLabel;


    private DemiurgeDungeonManager demiurgeDungeonManager;
    private DemiurgeContainerManager containerManager;
    private DemiurgeEndChecker endChecker;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO:comprobar los wearables
        crystalLabel.setVisible(false);
        werableLabel.setVisible(false);
        lblExit.setVisible(false);
        imgExit.setVisible(false);
    }

    //HUD solo hay en Home y Principal

    @Override
    public void principalCargado() {
        demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            roomId = this.getPrincipalController().getRoomId();
            room = demiurge.getDungeon().getRoom(roomId);
            wizard = demiurge.getWizard();
            //TODO: comprobaciones
            DungeonConfiguration dc = new DungeonConfiguration();
            Site site = new Room(roomId, "", null);
            containerManager = new DemiurgeContainerManager(demiurge.getWizard().getWearables(), demiurge.getWizard().getJewelryBag(), demiurge.getDungeon().getRoom(roomId).getContainer());
            endChecker = new DemiurgeEndChecker();
            demiurgeDungeonManager = new DemiurgeDungeonManager(dc, wizard, site , containerManager, endChecker);
            //
            List<Room> rooms = new ArrayList<>();
            demiurge.getDungeon().iterator().forEachRemaining(room -> rooms.add(room));
            List<Integer> roomIds = rooms.stream().map(Site::getID).toList();
            roomMoveComboBox.setItems(FXCollections.observableArrayList(roomIds));
        }
        List<String> items = new ArrayList<>();
        wizard.getCrystalCarrier().iterator().forEachRemaining(crystal -> items.add(crystal.toString()));
        wizard.getWearables().iterator().forEachRemaining(wearable -> items.add(wearable.toString()));
        wizard.getJewelryBag().iterator().forEachRemaining(jewelry -> items.add(jewelry.toString()));
        wizardItemsListView.setItems(FXCollections.observableArrayList(items));

        hasMonster = demiurge.getDungeon().getRoom(roomId).isAlive();
        if (hasMonster) {
            explorersMenuPane.setVisible(false);
            battleMenuPane.setVisible(true);
            imgMonster.setImage(new Image(getClass().getResource("/images/monster.png").toExternalForm()));
        } else {
            explorersMenuPane.setVisible(true);
            battleMenuPane.setVisible(false);
            if (room.isExit()){
                lblExit.setVisible(true);
                imgExit.setVisible(true);
            }
        }
    }

    @FXML
    void exchangeItemRoomBag(MouseEvent mouseEvent) {

    }

    @FXML
    void removeAction(ActionEvent actionEvent) {
        if (wizardItemsListView.getSelectionModel().getSelectedItem() != null) {
            String itemName = wizardItemsListView.getSelectionModel().getSelectedItem();
            Item item;
            wizard.getCrystalCarrier().iterator().forEachRemaining(o -> {
                if (o.toString().equals(itemName)) {

                }});
            wizard.getWearables().iterator().forEachRemaining(o -> {

                if (o.toString().equals(itemName)){}});
            wizard.getJewelryBag().iterator().forEachRemaining(o ->{

                if (o.toString().equals(itemName)) {}});

        }
    }

    @FXML
    void moveThere(ActionEvent actionEvent) {

    }

    @FXML
    void lookForItems(ActionEvent actionEvent) {
        crystalLabel.setVisible(true);
        if (demiurge.getDungeon().getRoom(roomId).isEmpty()) {
            imgCrystal.setVisible(false);
            crystalLabel.setText("No crystals in Room " + roomId);
        } else {
            imgCrystal.setVisible(true);
            crystalLabel.setText("Crystal");
            imgCrystal.setImage(new Image(getClass().getResource("/images/crystal.png").toExternalForm()));
        }
        werableLabel.setVisible(true);
        if (demiurge.getDungeon().getRoom(roomId).getContainer().isEmpty()) {
            imgWearable.setVisible(false);
            werableLabel.setVisible(false);
            werableLabel.setText("No objects in Room " + roomId);
        } else {
            imgWearable.setVisible(true);
            werableLabel.setVisible(true);
            Item item = demiurge.getDungeon().getRoom(roomId).getContainer().get(0);
            item.getClass().getSimpleName();
            if (item.getClass().getSimpleName().equals("Necklace")) {
                imgWearable.setImage(new Image(getClass().getResource("/images/necklace.png").toExternalForm()));
            } else if (item.getClass().getSimpleName().equals("Weapon")) {
                imgWearable.setImage(new Image(getClass().getResource("/images/sword.png").toExternalForm()));
            } else if (item.getClass().getSimpleName().equals("ring")) {
                imgWearable.setImage(new Image(getClass().getResource("/images/ring.png").toExternalForm()));
            }
            werableLabel.setText(item.toString());
        }
    }

    @FXML
    void pickWearable(MouseEvent mouseEvent) {
        try {
            Item item = demiurge.getDungeon().getRoom(roomId).getContainer().get(0);
            wizard.getWearables().add(item);
            demiurge.getDungeon().getRoom(roomId).getContainer().remove(0);
            imgWearable.setVisible(false);
        } catch (ContainerUnacceptedItemException | ContainerFullException e) {
            throw new RuntimeException(e);
        }
        saveAll();
    }

    @FXML
    void pickCrystal(MouseEvent mouseEvent) {
        demiurge.getDungeon().getRoom(roomId).gather();
        saveAll();
    }

    @FXML
    void performPhysicalAttack(ActionEvent actionEvent) {

        demiurge.getWizard().checkWeapon();
        //demiurgeDungeonManager.wizardAttack()


        //demiurge.getDungeon().getRoom(roomId).getMonster().receivePhysicalAttack(demiurge.getWizard().getWeapon().getDamage());
        //demiurge.getDungeon().getRoom(roomId/).ge().receivePhysicalAttack(demiurge.getWizard().ge().getDamage());
    }

    @FXML
    void castSpell(ActionEvent actionEvent) {
        //Una cosa, aqui no tendria que haber un combox cvon los hechizos aprendidos del mago?
        //Cosas por hacer:
        //Esta pantalla.
        ArrayList<Attack> attackList = new ArrayList<>();
        wizard.getAttacksIterator().forEachRemaining(attackList::add);
        spellComboBox.getItems().addAll(FXCollections.observableArrayList(attackList));
    }

    @FXML
    void runAway(ActionEvent actionEvent) {
        explorersMenuPane.setVisible(true);
        battleMenuPane.setVisible(false);
        imgMonster.setVisible(false);
    }

    private void saveAll() {
        demiurge.setWizard(wizard);
        this.getPrincipalController().setDemiurge(demiurge);
        this.getPrincipalController().goToRoom(roomId);
    }

}
