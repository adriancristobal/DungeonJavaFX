package ui.screens.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController extends BaseScreenController implements Initializable {

    @FXML
    private ImageView imgChestHomeHud;

    @FXML
    private ImageView imgComfortHomeHud;

    @FXML
    private ImageView imgSingaHomeHud;

    @FXML
    private ImageView imgSingaStorageHomeHud;
    @FXML
    private ComboBox homeSelectionFilterComboBox;
    @FXML
    private ScrollPane actionsLogScrollPane;
    @FXML
    private TextField numberOfCristalsTextView;

    @FXML
    void sleepAction(ActionEvent actionEvent) {
    }

    @FXML
    void manageStorageAction(ActionEvent actionEvent) {
    }

    @FXML
    void mergeCrystalsAction(ActionEvent actionEvent) {
    }

    @FXML
    void improveHomeAction(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alerta = new Alert(Alert.AlertType.NONE);
    }
}
