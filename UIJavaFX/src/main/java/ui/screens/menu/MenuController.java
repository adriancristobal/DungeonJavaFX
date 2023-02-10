package ui.screens.menu;

import game.DungeonLoaderXML;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.object.ItemCreationErrorException;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.SpellUnknowableException;
import game.util.ValueOverMaxException;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import ui.common.BaseScreenController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends BaseScreenController implements Initializable {

    @FXML
    private ComboBox<String> dropDownSavedGames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void loadGameAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game (.xml)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        this.getPrincipalController().loadManagers(file);
    }
}
