package ui.screens.dungeon;

import game.actions.Attack;
import game.character.Wizard;
import game.demiurge.Demiurge;
import game.dungeon.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ui.common.BaseScreenController;

import java.net.URL;
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
    public void principalCargado(){
        Demiurge demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            int roomId = this.getPrincipalController().getRoomId();
            room = demiurge.getDungeon().getRoom(roomId);
            wizard = demiurge.getWizard();
        }
    }


    @FXML
    private void manageItemsAction(ActionEvent actionEvent) {

    }

    @FXML
    private void lookItemsAction(ActionEvent actionEvent) {

    }

    //TODO: en el initialize poner que si hay criatura cargue el menú de batalla else cargue el menú dungeon

}
