package ui.screens.dungeon;

import game.actions.Attack;
import game.actions.PhysicalAttack;
import game.character.Wizard;
import game.character.exceptions.CharacterKilledException;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.*;
import game.dungeon.Room;
import game.dungeon.Site;
import game.object.Item;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    private ComboBox<Attack> spellComboBox;
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
    private ListView<Item> wizardItemsListView;
    @FXML
    private Text spellToCastLabel;
    @FXML
    private Button removeBtn;
    @FXML
    private Button startFightButton;
    @FXML
    private ImageView exchangeItemRoomBagBtn;
    @FXML
    private Text crystalLabel;
    @FXML
    private Text werableLabel;

    public boolean hasRun;

    private DemiurgeDungeonManager demiurgeDungeonManager;
    private DemiurgeContainerManager containerManager;
    private DemiurgeEndChecker endChecker;
    private DungeonConfiguration dc;

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
            dc = new DungeonConfiguration();
            roomId = this.getPrincipalController().getRoomId();
            room = demiurge.getDungeon().getRoom(roomId);
            wizard = demiurge.getWizard();
            containerManager = new DemiurgeContainerManager(demiurge.getWizard().getWearables(), demiurge.getWizard().getJewelryBag(), demiurge.getDungeon().getRoom(roomId).getContainer());
            endChecker = new DemiurgeEndChecker();
            demiurgeDungeonManager = new DemiurgeDungeonManager(dc, wizard, room, containerManager, endChecker);

            List<Room> rooms = new ArrayList<>();
            demiurge.getDungeon().iterator().forEachRemaining(rooms::add);
            List<String> roomIds;
            roomIds = rooms.stream().map(room1 -> String.valueOf(room1.getID())).toList();
            roomIds = roomIds.stream().filter(id -> !id.equals(String.valueOf(roomId))).toList();
            roomMoveComboBox.setItems(FXCollections.observableArrayList(roomIds));
        }
        updateWizardItems();

//        hasMonster = demiurge.getDungeon().getRoom(roomId).isAlive();
        hasMonster = demiurgeDungeonManager.hasCreature();
        if (hasMonster && !hasRun) {
            explorersMenuPane.setVisible(false);
            battleMenuPane.setVisible(true);
            imgMonster.setImage(new Image(getClass().getResource("/images/monster.png").toExternalForm()));
            physicalAttackBtn.setVisible(false);
            castSpellBtn.setVisible(false);
            spellComboBox.setVisible(false);
            spellToCastLabel.setVisible(false);
            if (!demiurgeDungeonManager.priority()) {
                monsterAttack();
            }
        } else {
            hasRun = true;
            explorersMenuPane.setVisible(true);
            battleMenuPane.setVisible(false);
            if (room.isExit()) {
                lblExit.setVisible(true);
                imgExit.setVisible(true);
                if (endChecker.check()) {
                    imgExit.setImage(new Image(getClass().getResource("/images/exit_open.jpg").toExternalForm()));
                } else {
                    imgExit.setImage(new Image(getClass().getResource("/images/exit_closed.jpg").toExternalForm()));
                }
            }
        }
    }

    private void updateWizardItems() {
        List<Item> items = new ArrayList<>();
        wizard.getCrystalCarrier().iterator().forEachRemaining(crystal -> items.add((Item) crystal));
        wizard.getWearables().iterator().forEachRemaining(wearable -> items.add((Item) wearable));
        wizard.getJewelryBag().iterator().forEachRemaining(jewelry -> items.add((Item) jewelry));
        wizardItemsListView.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    void exchangeItemRoomBag() {

    }

    @FXML
    void removeAction() {
        if (wizardItemsListView.getSelectionModel().getSelectedItem() != null) {
            int index = wizardItemsListView.getSelectionModel().getSelectedIndex();
            try {
                containerManager.addItem(wizard.getWearables(), index, containerManager.getSite());
                updateWizardItems();
            } catch (ContainerUnacceptedItemException e) {
                this.getPrincipalController().showErrorAlert("You can't save this item in this container");
            } catch (ContainerFullException e) {
                this.getPrincipalController().showErrorAlert("The Room is full, you can't drop items here");
            }
            saveAll();

        }
    }

    @FXML
    void moveThere() {
        saveAll();
        if (roomMoveComboBox.getSelectionModel().getSelectedItem() == "Home") {
            this.getPrincipalController().goHome();
        } else {
            //TODO: cambiar por dungeonManager.openDoor(*);
            // * la puerta seleccionada
            this.getPrincipalController().goToRoom(Integer.parseInt(roomMoveComboBox.getSelectionModel().getSelectedItem().toString()));
        }
        try {
            // con el open door no hace falta bajar energía
            wizard.drainEnergy(dc.getBasicEnergyConsumption());
            getPrincipalController().fillTexts();
        } catch (WizardTiredException e) {
            this.getPrincipalController().showErrorAlert("You are too tired!\nGoing back home to sleep");
            this.getPrincipalController().goHome();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void lookForItems() {
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
            } else if (item.getClass().getSimpleName().equals("Ring")) {
                imgWearable.setImage(new Image(getClass().getResource("/images/ring.png").toExternalForm()));
            }
            werableLabel.setText(item.toString());
        }
    }

    @FXML
    void pickWearable() {
        try {
            Item item = demiurge.getDungeon().getRoom(roomId).getContainer().get(0);
            wizard.getWearables().add(item);
            demiurge.getDungeon().getRoom(roomId).getContainer().remove(0);
            imgWearable.setVisible(false);
            updateWizardItems();
        } catch (ContainerUnacceptedItemException e) {
            this.getPrincipalController().showErrorAlert("That is not a valid item");
        } catch (ContainerFullException e) {
            this.getPrincipalController().showErrorAlert("Your bag is full, you can't pick up items");
        }
        saveAll();
    }

    @FXML
    void pickCrystal() {
        demiurgeDungeonManager.gatherCrystals();
        imgCrystal.setVisible(false);
        crystalLabel.setVisible(false);
        updateWizardItems();
        saveAll();
    }

    //TODO: poner botón de ataque con arma

    @FXML
    void performPhysicalAttack() {
        demiurge.getWizard().getAttacksIterator().forEachRemaining(attack -> {
            if (attack instanceof PhysicalAttack) {
                battle(attack);
            }
        });
    }

    @FXML
    void castSpell() {
        ArrayList<Attack> attackList = new ArrayList<>();
        wizard.getAttacksIterator().forEachRemaining(attackList::add);
        spellComboBox.getItems().addAll(FXCollections.observableArrayList(attackList));

        //TODO: comprobar si peta
        // si no peta, qué elemento devuelve el combo box... coge el tipo de hechizo?
        Attack attack = spellComboBox.getSelectionModel().getSelectedItem();
        if (attack != null) {
            battle(attack);
        } else {
            this.getPrincipalController().showErrorAlert("Choose a spell");
        }
    }

    private void battle(Attack attack) {
        boolean fightFinished = false;
        while (!fightFinished) {
            try {
                if (demiurgeDungeonManager.wizardAttack(attack)) {
                    this.getPrincipalController().fillHud();
                    //TODO: comprobar si se entiende sin mostrar mensaje?
                } else {
                    this.getPrincipalController().showInfoAlert("Attack failed. It's the monster's turn.");
                }

                fightFinished = monsterAttack();
            } catch (CharacterKilledException killedException) {
                this.getPrincipalController().showInfoAlert("You kill the monster!");
                fightFinished = true;
            } catch (WizardTiredException tiredException) {
                this.getPrincipalController().showErrorAlert("You are too tired!\nGoing back home to sleep");
                this.getPrincipalController().goHome();
            } catch (WizardNotEnoughEnergyException energyException) {
                this.getPrincipalController().showErrorAlert("You don't have enough energy to attack!");
            }
        }
    }

    private boolean monsterAttack() {
        boolean fightFinished = false;
        try {
            if (demiurgeDungeonManager.creatureAttack()) {
                this.getPrincipalController().fillHud();
            } else {
                this.getPrincipalController().showInfoAlert("Monster's attack failed. It's your turn!");
            }
        } catch (CharacterKilledException killedException) {
            this.getPrincipalController().showInfoAlert("The monster killed you!");
            fightFinished = true;
        }
        return fightFinished;
    }

    @FXML
    void runAway() {
        if (demiurgeDungeonManager.canRunAway()) {
            explorersMenuPane.setVisible(true);
            battleMenuPane.setVisible(false);
            imgMonster.setVisible(false);
            hasRun = true;
        } else {
            this.getPrincipalController().showInfoAlert("You can't run away.\nFIGHT!");
        }
    }

    private void saveAll() {
        demiurge.setWizard(wizard);
        this.getPrincipalController().setDemiurge(demiurge);
    }

    @FXML
    void startFight() {
        physicalAttackBtn.setVisible(true);
        castSpellBtn.setVisible(true);
        spellComboBox.setVisible(true);
        startFightButton.setVisible(false);
        spellToCastLabel.setVisible(true);
    }
}
