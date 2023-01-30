package ui.screens.battleScreen;

import jakarta.inject.Inject;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class BattleScreenController extends BaseScreenController implements Initializable {

    Alert alerta;

    @Inject
    public BattleScreenController() {
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
