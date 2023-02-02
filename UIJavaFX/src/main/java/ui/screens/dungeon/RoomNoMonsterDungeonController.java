package ui.screens.dungeon;

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

    @FXML
    public AnchorPane actionsLogScrollPane;
    private Room room;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //HUD solo hay en Home y Principal

    @Override
    public void principalCargado(){
        Demiurge demiurge = this.getPrincipalController().getDemiurge();
        if (demiurge != null) {
            int roomId = this.getPrincipalController().getRoomId();
            room = demiurge.getDungeon().getRoom(roomId);
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
