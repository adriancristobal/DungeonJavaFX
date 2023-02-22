package ui.screens.dungeon;

import game.actions.Attack;
import game.actions.PhysicalAttack;
import game.character.Wizard;
import game.character.exceptions.CharacterKilledException;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.*;
import game.dungeon.Door;
import game.dungeon.Home;
import game.dungeon.Room;
import game.dungeon.Site;
import game.object.Item;
import game.object.Weapon;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.Spell;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RoomNoMonsterDungeonController extends BaseScreenController implements Initializable {

    private int roomId;
    private Room room;

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
    public Button swordAttackBtn;
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
        imgWearable.setDisable(true);
        imgCrystal.setDisable(true);
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
            this.getPrincipalController().setCurrentSite(room);
            wizard = demiurge.getWizard();
            containerManager = new DemiurgeContainerManager(demiurge.getWizard().getWearables(), demiurge.getWizard().getJewelryBag(), room.getContainer());
            endChecker = new DemiurgeEndChecker();
//            demiurgeDungeonManager = new DemiurgeDungeonManager(dc, wizard, room, containerManager, endChecker);
            //SIN ESTO NO CARGA LA CRIATURA INICIAL
            demiurgeDungeonManager = this.getPrincipalController().getDungeonManager();

            List<String> doors = new ArrayList<>();
            this.getPrincipalController().getDungeonManager().getDoorsIterator().forEachRemaining(door -> {
                doors.add(door.showFrom(room).getDescription());
            });
            roomMoveComboBox.setItems(FXCollections.observableArrayList(doors));
        }
        updateWizardItems();

        if (demiurgeDungeonManager.hasCreature() && demiurgeDungeonManager.isAlive()) {
            demiurgeDungeonManager.checkWeapon();
            ArrayList<Attack> spells = new ArrayList<>();
            wizard.getAttacksIterator().forEachRemaining(attack -> {
                if (attack instanceof Spell) {
                    spells.add(attack);
                }
            });
            spellComboBox.getItems().addAll(FXCollections.observableArrayList(spells));
            explorersMenuPane.setVisible(false);
            battleMenuPane.setVisible(true);
            imgMonster.setImage(new Image(getClass().getResource("/images/monster.png").toExternalForm()));
            physicalAttackBtn.setVisible(false);
            swordAttackBtn.setVisible(false);
            castSpellBtn.setVisible(false);
            spellComboBox.setVisible(false);
            spellToCastLabel.setVisible(false);
            if (!demiurgeDungeonManager.priority()) {
                monsterAttack();
            }
        } else {
            exit();
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
        AtomicReference<Door> doorSelected = new AtomicReference<>();
        int doorIndex = roomMoveComboBox.getSelectionModel().getSelectedIndex();
        List<Door> list = new ArrayList<>();
        demiurgeDungeonManager.getDoorsIterator().forEachRemaining(list::add);
        Door door = list.get(doorIndex);
        getPrincipalController().fillTexts();
//        Site site = doorSelected.get().showFrom(room);
//        if (site instanceof Home) {
//
//            doorSelected.get().openFrom(room);
//            this.getPrincipalController().goHome();
//        } else {
//            AtomicInteger index = new AtomicInteger();
//            index.set(0);
//            Map<Integer, Integer> list = new HashMap<>();
//            demiurge.getDungeon().iterator().forEachRemaining(room1 -> {
//                list.put(room1.getID(), index.get());
//                index.addAndGet(1);
//            });
        Site nextRoom = door.showFrom(room);
//            int roomIndex = list.get(nextRoom.getID());
        this.getPrincipalController().goToRoom(doorIndex, nextRoom);
//        }
    }

    @FXML
    void lookForItems() {
        crystalLabel.setVisible(true);
        if (room.isEmpty()) {
            imgCrystal.setVisible(false);
            crystalLabel.setText("No crystals in Room " + roomId);
        } else {
            imgCrystal.setVisible(true);
            imgCrystal.setDisable(false);
            crystalLabel.setText("Crystal");
            imgCrystal.setImage(new Image(getClass().getResource("/images/crystal.png").toExternalForm()));
        }
        werableLabel.setVisible(true);
        if (room.getContainer().isEmpty()) {
            imgWearable.setVisible(false);
            werableLabel.setVisible(false);
            werableLabel.setText("No objects in Room " + roomId);
        } else {
            imgWearable.setVisible(true);
            imgWearable.setDisable(false);
            werableLabel.setVisible(true);
            Item item = room.getContainer().get(0);
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
            Item item = room.getContainer().get(0);
            wizard.getWearables().add(item);
            room.getContainer().remove(0);
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
        try {
            demiurgeDungeonManager.gatherCrystals();
        } catch (ContainerFullException e) {
            getPrincipalController().showErrorAlert("Your carrier is already full. You can't pick the crystal.");
        } catch (ContainerUnacceptedItemException e) {
            getPrincipalController().showErrorAlert("You can't put that in there.");
        }
        imgCrystal.setVisible(false);
        crystalLabel.setVisible(false);
        updateWizardItems();
        saveAll();
        getPrincipalController().refreshCrystalsHud();
    }

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
        Attack attack = spellComboBox.getSelectionModel().getSelectedItem();
        if (attack != null) {
            battle(attack);
        } else {
            this.getPrincipalController().showErrorAlert("Choose a spell");
        }
    }

    private void battle(Attack attack) {
        try {
            if (demiurgeDungeonManager.wizardAttack(attack)) {
                this.getPrincipalController().fillHud();
                this.getPrincipalController().showInfoAlert("You attacked successfully. It's the monster's turn");
            } else {
                this.getPrincipalController().showInfoAlert("Attack failed. It's the monster's turn.");
            }

            monsterAttack();
        } catch (CharacterKilledException killedException) {
            this.getPrincipalController().showInfoAlert("You kill the monster!");
            explorersMenuPane.setVisible(true);
            battleMenuPane.setVisible(false);
        } catch (WizardTiredException tiredException) {
            this.getPrincipalController().showErrorAlert("You are too tired!\nGoing back home to sleep");
            this.getPrincipalController().tired();
        } catch (WizardNotEnoughEnergyException energyException) {
            this.getPrincipalController().showErrorAlert("You don't have enough energy to attack!");
        }
    }

    private void monsterAttack() {
        try {
            if (demiurgeDungeonManager.creatureAttack()) {
                this.getPrincipalController().fillHud();
            } else {
                this.getPrincipalController().showInfoAlert("Monster's attack failed. It's your turn!");
            }
        } catch (CharacterKilledException killedException) {
            this.getPrincipalController().showInfoAlert("The monster killed you!");
            this.getPrincipalController().tired();
            //TODO: va a la casa?
        }
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
        swordAttackBtn.setVisible(true);
        castSpellBtn.setVisible(true);
        spellComboBox.setVisible(true);
        startFightButton.setVisible(false);
        spellToCastLabel.setVisible(true);
    }

    @FXML
    public void performSwordAttack(ActionEvent actionEvent) {
        demiurge.getWizard().getAttacksIterator().forEachRemaining(attack -> {
            if (attack instanceof Weapon) {
                battle(attack);
            }
        });
    }
}
