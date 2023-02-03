package ui.screens.home;

import game.character.Wizard;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.dungeon.Room;
import game.object.SingaCrystal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HomeController extends BaseScreenController implements Initializable {


    @FXML
    private Button improveHomeButton;
    @FXML
    private Button improveCharacterButton;
    @FXML
    private Button sleepButton;
    @FXML
    private Button mergeButton;
    @FXML
    private ComboBox<SingaCrystal> crystalSelectionHomeComboBox;
    @FXML
    private ComboBox<String> characterSelectionFilterComboBox;
    @FXML
    private ComboBox<String> homeSelectionFilterComboBox;
    @FXML
    private Text totalChestAmountHomeHud;
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
    private ImageView imgChestHomeHud;
    @FXML
    private ImageView imgComfortHomeHud;
    @FXML
    private ImageView imgSingaHomeHud;
    @FXML
    private ImageView imgSingaStorageHomeHud;

    private DungeonConfiguration dc;
    private Demiurge demiurge;
    private Home home;
    private Wizard wizard;

    Alert alerta;


    @FXML
    private void sleepAction(ActionEvent actionEvent) {
    }

    @FXML
    private void manageStorageAction(ActionEvent actionEvent) {
        getPrincipalController().goStorage();
    }

    @FXML
    private void mergeCrystalsAction(ActionEvent actionEvent) {
        crystalSelectionHomeComboBox.setDisable(false);
        loadCrystalForMergeComboBox();
    }

    @FXML
    private void mergeAction(ActionEvent actionEvent) {
        try {
            SingaCrystal cristalToMerge = crystalSelectionHomeComboBox.getValue();
            try {
                home.mergeCrystal(cristalToMerge);
            } catch (Exception e) {
                alert("Error", "Error to merge crystal", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            alert("Error", "Value is wrong", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void improveCharacter(ActionEvent actionEvent) {
        try {
            String itemWizardToImprove = characterSelectionFilterComboBox.getValue();
            if (itemWizardToImprove.equals("Max Life")) {
                try {
                    wizard.upgradeLifeMax(dc.getBasicIncrease());
                } catch (Exception e){
                    alert("Error","You don't have enough life to upgrade your life", Alert.AlertType.ERROR);
                }
            } else if (itemWizardToImprove.equals("Max Capacity")) {
                try {
                    wizard.upgradeEnergyMax(dc.getBasicIncrease());
                } catch (Exception e) {
                    alert("Error","You don't have enough energy to upgrade your energy", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            alert("Warning", "Value incorrect", Alert.AlertType.WARNING);
        }
    }
    @FXML
    void improveHomeAction(ActionEvent actionEvent) {
        try {
            String itemHomeToImprove = homeSelectionFilterComboBox.getValue();
            if (itemHomeToImprove.equals("Comfort")) {
                try {
                    home.upgradeComfort();
                } catch (Exception e){
                    alert("Error","You don't have enough Singa to improve your Comfort", Alert.AlertType.ERROR);
                }
            } else if (itemHomeToImprove.equals("Stone Capacity")) {
                try {
                    //singaStorageAmountHomeHud.getSelectionEnd();
                    home.upgradeMaxSinga(dc.getStoneIncrease());
                } catch (Exception e) {
                    alert("Error","You don't have enough Singa to improve your Stone Capacity", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            alert("Warning", "Value incorrect", Alert.AlertType.WARNING);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alerta = new Alert(Alert.AlertType.NONE);
        loadComboBoxesUpgrade();
        //loadCrystalForMergeComboBox();
    }

    private void alert(String titulo, String texto, Alert.AlertType tipo){
        alerta.setTitle(titulo);
        alerta.setContentText(texto);
        alerta.setAlertType(tipo);
        alerta.showAndWait();
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

    private void loadComboBoxesUpgrade() {
        homeSelectionFilterComboBox.getItems().clear();
        characterSelectionFilterComboBox.getItems().clear();
        try {
            homeSelectionFilterComboBox.getItems().addAll("Comfort", "Stone capacity");
            characterSelectionFilterComboBox.getItems().addAll("Max life", "Max capacity");
        } catch (Exception e) {
            alert("Error", "Error to load the home or wizard upgrades", Alert.AlertType.ERROR);
        }
    }

    private void loadCrystalForMergeComboBox() {
        crystalSelectionHomeComboBox.getItems().clear();
        try {
            for (Iterator it = wizard.getCrystalCarrier().iterator(); it.hasNext(); ) {
                SingaCrystal singaCrystal = (SingaCrystal) it.next();
                crystalSelectionHomeComboBox.getItems().add(singaCrystal);
            }
        } catch (Exception e) {
            alert("Error", "Error to load the home or wizard upgrades", Alert.AlertType.ERROR);
        }
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

    @FXML
    private void enableButtonImproveCharacterAction(ActionEvent actionEvent) {
        if (characterSelectionFilterComboBox != null) {
            improveCharacterButton.setDisable(false);
        }
    }

    @FXML
    private void enableButtonImproveHomeAction(ActionEvent actionEvent) {
        if (homeSelectionFilterComboBox != null) {
            improveHomeButton.setDisable(false);
        }
    }

    @FXML
    private void enableButtonMerge(ActionEvent actionEvent) {
        if (crystalSelectionHomeComboBox != null) {
            mergeButton.setDisable(false);
        }
    }

}
