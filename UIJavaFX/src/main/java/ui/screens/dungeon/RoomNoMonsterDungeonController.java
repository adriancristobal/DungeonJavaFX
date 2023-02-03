package ui.screens.dungeon;

import game.actions.Attack;
import game.character.Wizard;
import game.demiurge.Demiurge;
import game.dungeon.Room;
import game.spell.Spell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoomNoMonsterDungeonController extends BaseScreenController implements Initializable {

    private Room room;

    private boolean hasMonster;
    private Demiurge demiurge;
    private Wizard wizard;
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
    private ListView wizardItemsListView;
    @FXML
    private Button removeBtn;
    @FXML
    private ImageView exchangeItemRoomBagBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: cargar el menu batalla si es true
        hasMonster = room.isAlive();
        if (hasMonster){

        }else {

        }
    }

    //HUD solo hay en Home y Principal

    @Override
    public void principalCargado() {
        demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            int roomId = this.getPrincipalController().getRoomId();
            room = demiurge.getDungeon().getRoom(roomId);
            wizard = demiurge.getWizard();
        }
    }

    @FXML
    void exchangeItemRoomBag(MouseEvent mouseEvent) {

    }

    @FXML
    void removeAction(ActionEvent actionEvent) {
    }

    @FXML
    void moveThere(ActionEvent actionEvent) {
    }

    @FXML
    void lookForItems(ActionEvent actionEvent) {
    }

    @FXML
    void pickWearable(MouseEvent mouseEvent) {
    }

    @FXML
    void pickCrystal(MouseEvent mouseEvent) {
            room.gather();
    }

    @FXML
    void performPhysicalAttack(ActionEvent actionEvent) {

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

    }


}
