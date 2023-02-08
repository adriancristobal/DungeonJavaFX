package ui.screens.home;

import game.character.Wizard;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.Demiurge;
import game.demiurge.DemiurgeContainerManager;
import game.demiurge.DemiurgeHomeManager;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.dungeon.HomeNotEnoughSingaException;
import game.dungeon.Room;
import game.object.SingaCrystal;
import game.objectContainer.Container;
import game.objectContainer.exceptions.ContainerEmptyException;
import game.objectContainer.exceptions.ContainerErrorException;
import game.util.ValueOverMaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ui.common.BaseScreenController;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HomeController extends BaseScreenController implements Initializable {


    @FXML
    private ImageView imgBedHome;
    @FXML
    private ImageView imgSpellBookHome;
    @FXML
    private TextField tfLifePointsToRecover;
    @FXML
    private ImageView imgImproveLifeHealth;
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

    private Demiurge demiurge;
    private Home home;
    private Wizard wizard;
    private DemiurgeHomeManager demiurgeHomeManager;
    Alert alerta;


    @FXML
    private void sleepAction(ActionEvent actionEvent) {
        try {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            getPrincipalController().fillTexts();
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
           // crystalSelectionHomeComboBox.getSelectionModel().select();
            SingaCrystal cristalToMerge = crystalSelectionHomeComboBox.getValue();
            try {
                demiurgeHomeManager.mergeCrystal(cristalToMerge.getValue());
                singaAmountHomeHud.setText(String.valueOf(home.getSinga()));
                crystalSelectionHomeComboBox.setValue(null);
                crystalSelectionHomeComboBox.setDisable(true);
                mergeButton.setDisable(true);
                alert("Info", "You have successfully merged the crystal", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                alert("Error", "Error to merge crystal", Alert.AlertType.ERROR);
            } catch (WizardTiredException e) {
                alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
                throw new RuntimeException(e);
            } catch (ContainerErrorException e) {
                alert("Error", "Error to merge crystal", Alert.AlertType.ERROR);
                throw new RuntimeException(e);
            } catch (ContainerEmptyException e) {
                alert("Error", "Error to merge crystal", Alert.AlertType.ERROR);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            alert("Error", "Value is wrong", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void improveCharacter(ActionEvent actionEvent) {

        String itemWizardToImprove = characterSelectionFilterComboBox.getValue();
        if (itemWizardToImprove != null) {
            if (itemWizardToImprove.equalsIgnoreCase("Max Life")) {
                try {
                    demiurgeHomeManager.upgradeLifeMax();
                } catch (WizardTiredException e) {
                    getPrincipalController().fillTexts();
                    characterSelectionFilterComboBox.setValue(null);
                    demiurge.nextDay();
                    getPrincipalController().refreshDay();
                    getPrincipalController().fillTexts();
                    alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
                    throw new RuntimeException(e);
                } catch (WizardNotEnoughEnergyException e) {
                    alert("Error","You don't have enough energy to upgrade your life", Alert.AlertType.ERROR);
                    throw new RuntimeException(e);
                } catch (HomeNotEnoughSingaException e) {
                    alert("Error","You don't have enough singa to upgrade your life", Alert.AlertType.ERROR);
                    throw new RuntimeException(e);
                }
            } else if (itemWizardToImprove.equalsIgnoreCase("Max Energy")) {
                try {
                    demiurgeHomeManager.upgradeEnergyMax();
                } catch (WizardTiredException e) {
                    getPrincipalController().fillTexts();
                    characterSelectionFilterComboBox.setValue(null);
                    demiurge.nextDay();
                    getPrincipalController().refreshDay();
                    getPrincipalController().fillTexts();
                    alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
                    throw new RuntimeException(e);
                } catch (WizardNotEnoughEnergyException e) {
                    alert("Error","You don't have enough energy to upgrade your max energy", Alert.AlertType.ERROR);
                    throw new RuntimeException(e);
                } catch (HomeNotEnoughSingaException e) {
                    alert("Error","You don't have enough singa to upgrade your max energy", Alert.AlertType.ERROR);
                    throw new RuntimeException(e);
                }
            }
        } else {
            alert("Warning", "Please select a value", Alert.AlertType.WARNING);
        }
    }
    @FXML
    void improveHomeAction(ActionEvent actionEvent) {

            String itemHomeToImprove = homeSelectionFilterComboBox.getValue();
            if (itemHomeToImprove != null) {

                if (itemHomeToImprove.equalsIgnoreCase("Comfort")) {
                    try {
                        demiurgeHomeManager.upgradeComfort();
                        setTextViews();
                        homeSelectionFilterComboBox.setValue(null);
                        demiurge.nextDay();
                        getPrincipalController().refreshDay();
                        getPrincipalController().fillTexts();
                        alert("Info", "You have successfully upgraded your home", Alert.AlertType.INFORMATION);
                    } catch (WizardTiredException e) {
                        alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
                        throw new RuntimeException(e);
                    } catch (WizardNotEnoughEnergyException e) {
                        alert("Error", "You don't have enough energy to upgrade your comfort", Alert.AlertType.ERROR);
                        throw new RuntimeException(e);
                    } catch (HomeNotEnoughSingaException e) {
                        alert("Error", "You don't have enough singa to upgrade your comfort", Alert.AlertType.ERROR);
                        throw new RuntimeException(e);
                    }
                } else if (itemHomeToImprove.equalsIgnoreCase("Singa Capacity")) {
                    try {
                        demiurgeHomeManager.upgradeSingaMax();
                    } catch (WizardTiredException e) {
                        setTextViews();
                        homeSelectionFilterComboBox.setValue(null);
                        demiurge.nextDay();
                        getPrincipalController().refreshDay();
                        getPrincipalController().fillTexts();
                        alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
                        throw new RuntimeException(e);
                    } catch (WizardNotEnoughEnergyException e) {
                        alert("Error", "You don't have enough energy to upgrade your singa capacity", Alert.AlertType.ERROR);
                        throw new RuntimeException(e);
                    } catch (HomeNotEnoughSingaException e) {
                        alert("Error", "You don't have enough singa to upgrade your singa capacity", Alert.AlertType.ERROR);
                        throw new RuntimeException(e);
                    }
                }
            } else {
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
            DungeonConfiguration dc = new DungeonConfiguration();
            //demiurgeContainerManager = new DemiurgeContainerManager(null, null, null);

            demiurgeHomeManager = new DemiurgeHomeManager(dc, wizard, home, null);
            fillHud();
            fillImagesHome();
        }
//        if (this.getPrincipalController().getDemiurge() != null) {
//            home = this.getPrincipalController().getDemiurge().getHome();
//            wizard = this.getPrincipalController().getDemiurge().getWizard();
//        }
    }

    private void fillImagesHome() {
        imgImproveLifeHealth.setImage(new Image(getClass().getResource("/images/health-care.png").toExternalForm()));
        imgSpellBookHome.setImage(new Image(getClass().getResource("/images/spellbook.png").toExternalForm()));
        imgBedHome.setImage(new Image(getClass().getResource("/images/bed.png").toExternalForm()));

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
            homeSelectionFilterComboBox.getItems().addAll("Comfort", "Singa Capacity");
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

    @FXML
    private void recoverLife(MouseEvent mouseEvent) {
        try {
            if (!tfLifePointsToRecover.getText().isEmpty() || tfLifePointsToRecover.getText().matches("[0-9]+" )) {
                demiurgeHomeManager.recover(Integer.parseInt(tfLifePointsToRecover.getText()));
                setTextViews();
                getPrincipalController().fillTexts();
            } else {
                alert("Warning", "Please, enter a valid number", Alert.AlertType.WARNING);
            }
        } catch (WizardTiredException e) {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            getPrincipalController().fillTexts();
            alert("Ups...","You are tired, you need to sleep", Alert.AlertType.INFORMATION);
            throw new RuntimeException(e);
        } catch (ValueOverMaxException e) {
            alert("Ups...","You are trying to recover more than your max life", Alert.AlertType.INFORMATION);
            throw new RuntimeException(e);
        } catch (HomeNotEnoughSingaException e) {
            alert("Ups...","You don't have enough Singa", Alert.AlertType.INFORMATION);
            throw new RuntimeException(e);
        }
    }
}
