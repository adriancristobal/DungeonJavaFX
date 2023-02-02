package ui.screens.home;

import game.character.Wizard;
import game.demiurge.Demiurge;
import game.dungeon.Home;
import game.dungeon.Room;
import game.objectContainer.Chest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends BaseScreenController implements Initializable {

    @FXML
    private Text chestAmountHomeHud;
    @FXML
    private Text singaStorageAmountHomeHud;
    @FXML
    private Text comfortAmountHomeHud;
    @FXML
    private Text singaAmountHomeHud;
    @FXML
    private Text totalSingaAmountHomeHud;
    @FXML
    private TextField numberOfCristalsTextView;
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
    private Demiurge demiurge;
    private Home home;
    private Wizard wizard;

    private Chest chest;


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
        loadComboBoxesUpgrade();
        loadCrystalForMergeComboBox();
    }

    @Override
    public void principalCargado() {
        if (this.getPrincipalController().getDemiurge() != null) {
            demiurge = this.getPrincipalController().getDemiurge();
            home = demiurge.getHome();
            wizard = demiurge.getWizard();
            fillHud();
        }
//        if (this.getPrincipalController().getDemiurge() != null) {
//            home = this.getPrincipalController().getDemiurge().getHome();
//            wizard = this.getPrincipalController().getDemiurge().getWizard();
//        }
    }

    //FILLS HOME HUD
    private void fillHud() {
        //TEXT
        setTextViews();

        //IMAGES
        imgChestHomeHud.setImage(new Image(getClass().getResource("/images/treasure.png").toExternalForm()));
        imgComfortHomeHud.setImage(new Image(getClass().getResource("/images/sofa.png").toExternalForm()));
        imgSingaHomeHud.setImage(new Image(getClass().getResource("/images/singa.png").toExternalForm()));
        imgSingaStorageHomeHud.setImage(new Image(getClass().getResource("/images/open-box.png").toExternalForm()));
    }

    private void setTextViews() {
        if (this.getPrincipalController().getDemiurge() != null) {
            comfortAmountHomeHud.setText(String.valueOf(home.getComfort()));
            singaAmountHomeHud.setText(String.valueOf(home.getSinga()));
            totalSingaAmountHomeHud.setText(String.valueOf(home.getMaxSinga()));
            singaStorageAmountHomeHud.setText(String.valueOf(home.getSingaSpace()));
            //esto ultimo no creo que este bien
            chestAmountHomeHud.setText(String.valueOf(home.getContainer().getValue()));
            totalChestAmountHomeHud.setText(String.valueOf(home.getContainer().size()));
        }


        //el numberofcristals es del mago.
        //numberOfCristalsTextView.setText(String.valueOf(wizard.getCrystalCarrier().getValue()));

    }

    private void saveAll() {
        demiurge.setHome(home);
        demiurge.setWizard(wizard);
        this.getPrincipalController().setDemiurge(demiurge);
    }

    @FXML
    private void goDungeonAction(ActionEvent actionEvent) {
        Room room = demiurge.getDungeon().iterator().next();
        getPrincipalController().goToRoom(room.getID());
    }
}
