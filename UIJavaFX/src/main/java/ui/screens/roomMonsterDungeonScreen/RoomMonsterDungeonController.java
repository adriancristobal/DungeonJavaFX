package ui.screens.roomMonsterDungeonScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomMonsterDungeonController extends BaseScreenController implements Initializable {

    Alert alerta;
    @FXML
    private ScrollPane actionsLogScrollPane;
    @FXML
    private ComboBox<String> spellSelectionComboBox;

    @FXML
    private void runAction(ActionEvent actionEvent) {
    }

    @FXML
    private void physicalAttackAction(ActionEvent actionEvent) {
    }

    @FXML
    private void castSpellAction(ActionEvent actionEvent) {
    }

    @FXML
    private void fightAction(ActionEvent actionEvent) {
    }

    private void alert(String titulo, String texto, Alert.AlertType tipo){
        alerta.setTitle(titulo);
        alerta.setContentText(texto);
        alerta.setAlertType(tipo);
        alerta.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alerta = new Alert(Alert.AlertType.NONE);
    }
}
