package ui.screens.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends BaseScreenController implements Initializable {
    @FXML
    private ComboBox<String> dropDownSavedGames;

    @FXML
    void createDungeonAction(ActionEvent event) {

    }

    @FXML
    void loadGameAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game (.xml)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //show file chooser
        fileChooser.showOpenDialog(null);
    }

    @FXML
    void newGameAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
