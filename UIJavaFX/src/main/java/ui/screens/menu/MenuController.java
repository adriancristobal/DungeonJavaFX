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

    private final DungeonLoaderXML loader;

    @Inject
    public MenuController(DungeonLoaderXML loader) {
        this.loader = loader;
    }

    @FXML
    private ComboBox<String> dropDownSavedGames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    @FXML
    private void loadGameAction(ActionEvent event) {
        try {
            DungeonConfiguration config = new DungeonConfiguration();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game (.xml)");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            Demiurge demiurge = new Demiurge();
            loader.load(demiurge, config, file);
            this.getPrincipalController().setDemiurgeFromLoad(demiurge);
        } catch (ValueOverMaxException valueMaxEx) {
            valueMaxEx.printStackTrace();
            this.getPrincipalController().showErrorAlert("Value is too high");
        } catch (ContainerFullException fullEx) {
            fullEx.printStackTrace();
            this.getPrincipalController().showErrorAlert("Container is full");
        } catch (ContainerUnacceptedItemException itemContEx) {
            itemContEx.printStackTrace();
            this.getPrincipalController().showErrorAlert("Container can't keep this item");
        } catch (ItemCreationErrorException itemCreationEx) {
            itemCreationEx.printStackTrace();
            this.getPrincipalController().showErrorAlert("Error creating item");
        } catch (SpellUnknowableException spellEx) {
            spellEx.printStackTrace();
            this.getPrincipalController().showErrorAlert("Unkown spell");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void newGameAction() {
        getPrincipalController().goHome();
    }
}
