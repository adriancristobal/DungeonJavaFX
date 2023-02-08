package ui.screens.home;

import game.character.Wizard;
import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.dungeon.Room;
import game.object.SingaCrystal;
import game.objectContainer.Container;
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

    private DungeonConfiguration dc;
    private Demiurge demiurge;
    private Home home;
    private Wizard wizard;

    Alert alerta;


    @FXML
    private void sleepAction(ActionEvent actionEvent) {
        try {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            alert("Shhh...", "You slept successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            alert("Error", "Error to sleep", Alert.AlertType.ERROR);
        }

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
                singaAmountHomeHud.setText(String.valueOf(home.getSinga()));
                crystalSelectionHomeComboBox.setValue(null);
                crystalSelectionHomeComboBox.setDisable(true);
                mergeButton.setDisable(true);
                alert("Info", "You have successfully merged the crystal", Alert.AlertType.INFORMATION);
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
            if (itemWizardToImprove.equalsIgnoreCase("Max Life")) {
                try {
                    wizard.upgradeLifeMax(dc.getBasicIncrease());
                    //upgrade maxLife of wizard
                    getPrincipalController().setMaxLifeAmountBotHud(getPrincipalController().getMaxLifeAmountBotHud());
                    characterSelectionFilterComboBox.setValue(null);
                    alert("Info", "You have updated the life successfully", Alert.AlertType.INFORMATION);
                } catch (Exception e){
                    alert("Error","You don't have enough singa/energy to upgrade your life", Alert.AlertType.ERROR);
                } catch (WizardTiredException e) {
                    throw new RuntimeException(e);
                } catch (WizardNotEnoughEnergyException e) {
                    throw new RuntimeException(e);
                } catch (HomeNotEnoughSingaException e) {
                    throw new RuntimeException(e);
                }
            } else if (itemWizardToImprove.equalsIgnoreCase("Max Energy")) {
                try {
                    wizard.upgradeEnergyMax(dc.getBasicIncrease());
                    //upgrade maxEnergy of wizard
                    getPrincipalController().setMaxEnergyAmountBotHud(getPrincipalController().getMaxEnergyAmountBotHud());
                    characterSelectionFilterComboBox.setValue(null);
                    alert("Info", "You have updated the max energy successfully", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    alert("Error","You don't have enough singa/energy to upgrade your energy", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            alert("Warning", "Please select a value", Alert.AlertType.WARNING);
        }
    }
    @FXML
    void improveHomeAction(ActionEvent actionEvent) {
        try {
            String itemHomeToImprove = homeSelectionFilterComboBox.getValue();
            if (itemHomeToImprove.equals("Comfort")) {
                try {
                    //TODO: averiguar si esto gasta singa/energy
                    demiurgeHomeManager.upgradeComfort();
                    //comfortAmountHomeHud.setText(String.valueOf(home.getComfort()));
                    setTextViews();
                    homeSelectionFilterComboBox.setValue(null);
                    alert("Info", "You have successfully updated the comfort level", Alert.AlertType.INFORMATION);
                } catch (Exception e){
                    alert("Error","You don't have enough singa to improve your maximum comfort lebeñ", Alert.AlertType.ERROR);
                }
            } else if (itemHomeToImprove.equals("Singa Capacity")) {
                try {
                    //singaStorageAmountHomeHud.getSelectionEnd();
                    home.upgradeMaxSinga(dc.getStoneIncrease());
                    //upgrade maxLife of wizard
                    singaStorageAmountHomeHud.setText(String.valueOf(home.getSingaSpace()));
                    homeSelectionFilterComboBox.setValue(null);
                    alert("Info", "You have successfully updated the maximum singa capacity", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    alert("Error","You don't have enough Singa to improve your Stone Capacity", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            alert("Warning", "Please select a value", Alert.AlertType.WARNING);
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
    }

    private void setTextViews() {
        if (this.getPrincipalController().getDemiurge() != null) {
            comfortAmountHomeHud.setText(String.valueOf(home.getComfort()));
            singaAmountHomeHud.setText(String.valueOf(home.getSinga()));
            totalSingaAmountHomeHud.setText(String.valueOf(home.getMaxSinga()));
            chestAmountHomeHud.setText(String.valueOf(home.getContainer().size()));
            totalChestAmountHomeHud.setText(String.valueOf(home.getContainer().getValue()));

        }


        //el numberofcristals es del mago.
        //numberOfCristalsTextView.setText(String.valueOf(wizard.getCrystalCarrier().getValue()));

    }

    private void loadComboBoxesUpgrade() {
        homeSelectionFilterComboBox.getItems().clear();
        characterSelectionFilterComboBox.getItems().clear();
        try {
            homeSelectionFilterComboBox.getItems().addAll("Comfort", "Singa capacity");
            characterSelectionFilterComboBox.getItems().addAll("Max Life", "Max Energy");
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
        saveAll();
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
