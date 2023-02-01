package ui.screens.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends BaseScreenController implements Initializable {

    Alert alerta;
    @FXML
    private ComboBox<String> dropDownSavedGames;

    @FXML
    private void loadGameAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game (.xml)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //show file chooser
        fileChooser.showOpenDialog(null);
    }

    @FXML
    private void newGameAction(ActionEvent event) {

        getPrincipalController().goHome();
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
