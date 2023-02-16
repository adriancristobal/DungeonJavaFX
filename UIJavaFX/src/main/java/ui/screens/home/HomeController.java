package ui.screens.home;

import game.character.Wizard;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardSpellKnownException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.Demiurge;
import game.demiurge.DemiurgeHomeManager;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.dungeon.HomeNotEnoughSingaException;
import game.dungeon.Room;
import game.object.SingaCrystal;
import game.objectContainer.exceptions.ContainerEmptyException;
import game.objectContainer.exceptions.ContainerErrorException;
import game.spell.Spell;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HomeController extends BaseScreenController implements Initializable {

    //TODO: En hechizos aprender, no se borra de la lista
    //TODO: Al mejorar hechizo, no se actualiza la lista

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alerta = new Alert(Alert.AlertType.NONE);
        loadComboBoxesUpgrade();
        listViewSpells.setVisible(false);
        //loadCrystalForMergeComboBox();
    }

    @FXML
    public ListView<Spell> listViewSpells;
    @FXML
    public ImageView imgLevelUpSpellHome;
    @FXML
    private ImageView imgBedHome;
    @FXML
    private ImageView imgSpellBookHome;
    @FXML
    private TextField tfLifePointsToRecover;
    @FXML
    private ImageView imgImproveLifeHealth;
    @FXML
    public Button learnSpellBtnHome;
    @FXML
    private Button improveHomeButton;
    @FXML
    private Button improveCharacterButton;
    @FXML
    private Button mergeButton;
    @FXML
    public ComboBox<Spell> spellComboBox;
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
    private void sleepAction(MouseEvent actionEvent) {
        try {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            getPrincipalController().fillTexts();
            getPrincipalController().showInfoAlert("You slept successfully");
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("Unable to sleep. There was an error");
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
            for (int i = 0; i < crystalSelectionHomeComboBox.getItems().size(); i++) {
                if (crystalSelectionHomeComboBox.getItems().get(i).equals(cristalToMerge)) {

                    demiurgeHomeManager.mergeCrystal(i);
                    alert("Success", "You merged the crystals successfully", Alert.AlertType.INFORMATION);
                    //crystalSelectionHomeComboBox.getItems().remove(i);
                    crystalSelectionHomeComboBox.getSelectionModel().clearSelection();
                    crystalSelectionHomeComboBox.setDisable(true);
                    mergeButton.setDisable(true);
                    setTextViews();
                }
            }
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("Value is wrong");
        } catch (WizardTiredException e) {
            getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
        } catch (ContainerErrorException e) {
            getPrincipalController().showErrorAlert("Error to merge crystal");
        } catch (ContainerEmptyException e) {
            getPrincipalController().showErrorAlert("You have no crystals to merge");
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
                    characterSelectionFilterComboBox.setValue(null);
                    demiurge.nextDay();
                    getPrincipalController().refreshDay();
                    getPrincipalController().fillTexts();
                    setTextViews();
                    getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
                } catch (WizardNotEnoughEnergyException e) {
                    getPrincipalController().showErrorAlert("You don't have enough energy to perform this action");
                } catch (HomeNotEnoughSingaException e) {
                    getPrincipalController().showErrorAlert("You don't have enough singa to perform this action");
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
                    setTextViews();
                    getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
                } catch (WizardNotEnoughEnergyException e) {
                    getPrincipalController().showErrorAlert("You don't have enough energy to perform this action");
                } catch (HomeNotEnoughSingaException e) {
                    getPrincipalController().showErrorAlert("You don't have enough singa to perform this action");
                }
            }
        } else {
            getPrincipalController().showWarningAlert("Please select a value");
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
                    getPrincipalController().showInfoAlert("You have successfully upgraded your home");
                } catch (WizardTiredException e) {
                    getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
                } catch (WizardNotEnoughEnergyException e) {
                    getPrincipalController().showErrorAlert("You don't have enough energy to perform this action");
                } catch (HomeNotEnoughSingaException e) {
                    getPrincipalController().showErrorAlert("You don't have enough singa to perform this action");
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
                    getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
                } catch (WizardNotEnoughEnergyException e) {
                    getPrincipalController().showErrorAlert("You don't have enough energy to upgrade your singa capacity");
                } catch (HomeNotEnoughSingaException e) {
                    getPrincipalController().showErrorAlert("You don't have enough singa to upgrade your singa capacity");
                }
            }
        } else {
            getPrincipalController().showWarningAlert("Please select a value");
        }
    }


    private void alert(String titulo, String texto, Alert.AlertType tipo) {
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
            loadSpellComboBox();
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
        imgLevelUpSpellHome.setImage(new Image(getClass().getResource("/images/level-up.png").toExternalForm()));

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

    }

    private void loadListViewSpells() {
        listViewSpells.getItems().clear();
        try {
            listViewSpells.getItems().addAll(getWizardMemory());
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("Unable to load the learnt spells");
        }
    }

    private void loadSpellComboBox() {
        spellComboBox.getItems().clear();
        try {
            spellComboBox.getItems().addAll(getHomeLibrary());
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("Unable to load the spells in the library");
        }
    }


    private void loadComboBoxesUpgrade() {
        homeSelectionFilterComboBox.getItems().clear();
        characterSelectionFilterComboBox.getItems().clear();
        try {
            homeSelectionFilterComboBox.getItems().addAll("Comfort", "Singa Capacity");
            characterSelectionFilterComboBox.getItems().addAll("Max Life", "Max Energy");
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("Unable to load the upgrade options");
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
            getPrincipalController().showErrorAlert("Unable to load the crystals in your carrier");
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
            if (!tfLifePointsToRecover.getText().isEmpty() || tfLifePointsToRecover.getText().matches("[0-9]+")) {
                demiurgeHomeManager.recover(Integer.parseInt(tfLifePointsToRecover.getText()));
                setTextViews();
                getPrincipalController().fillTexts();
                tfLifePointsToRecover.clear();
            } else {
                getPrincipalController().showWarningAlert("Please, enter a valid number");
            }
        } catch (WizardTiredException e) {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            getPrincipalController().fillTexts();
            getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
        } catch (ValueOverMaxException e) {
            getPrincipalController().showErrorAlert("Your life is already at a maximum");
        } catch (HomeNotEnoughSingaException e) {
            getPrincipalController().showErrorAlert("You don't have enough Singa to perform this action");
        }
    }

    @FXML
    private void seeLearntSpells(MouseEvent mouseEvent) {
        try {
            loadListViewSpells();
            listViewSpells.setVisible(!listViewSpells.isVisible());
        } catch (Exception e) {
            getPrincipalController().showErrorAlert("You don't know any spells yet!");
        }
    }

    @FXML
    private void learnSpell(ActionEvent actionEvent) {
        boolean spellWasLearnt = false;
        try {
            Spell spellToLearn = spellComboBox.getSelectionModel().getSelectedItem();

            if (listViewSpells.getItems().isEmpty()) {
                demiurgeHomeManager.learnSpell(spellComboBox.getSelectionModel().getSelectedIndex());
                loadSpellComboBox();
                setTextViews();
                getPrincipalController().fillTexts();
                getPrincipalController().showInfoAlert("The spell was learnt successfully");
            } else {
                for (Spell spellLeart : listViewSpells.getItems()) {
                    if (spellToLearn == spellLeart) {
                        spellWasLearnt = true;
                        break;
                    }
                }
                if (spellWasLearnt) {
                    getPrincipalController().showErrorAlert("You already know this spell!");
                } else {
                    demiurgeHomeManager.learnSpell(spellComboBox.getSelectionModel().getSelectedIndex());
                    loadSpellComboBox();
                    getPrincipalController().showInfoAlert("The spell was learnt successfully");
                }


            }

        } catch (Exception e) {
            getPrincipalController().showWarningAlert("You didn't select any spell");
        } catch (WizardTiredException e) {
            getPrincipalController().showInfoAlert("You are tired and need to go to sleep");
        } catch (WizardSpellKnownException e) {
            getPrincipalController().showErrorAlert("You already know this spell");
        } catch (WizardNotEnoughEnergyException e) {
            getPrincipalController().showErrorAlert("You don't have enough energy to perform this action");
        } catch (HomeNotEnoughSingaException e) {
            getPrincipalController().showErrorAlert("You don't have enough singa to perform this action");
        }
    }

    @FXML
    private void upgradeSpell(MouseEvent mouseEvent) {
        try {
            demiurgeHomeManager.improveSpell(listViewSpells.getSelectionModel().getSelectedIndex());
            loadListViewSpells();
            setTextViews();
            getPrincipalController().fillTexts();
            getPrincipalController().showInfoAlert("The spell was improved successfully");
        } catch (Exception e) {
            getPrincipalController().showWarningAlert("You didn't select any spell");
        } catch (WizardTiredException e) {
            demiurge.nextDay();
            getPrincipalController().refreshDay();
            getPrincipalController().fillTexts();
            getPrincipalController().showInfoAlert("You are tired, you need to go to sleep");
        } catch (WizardNotEnoughEnergyException e) {
            getPrincipalController().showErrorAlert("You don't have enough energy to perform this action");
        } catch (ValueOverMaxException e) {
            getPrincipalController().showErrorAlert("This spell has already been improved to its maximum level");
        } catch (HomeNotEnoughSingaException e) {
            getPrincipalController().showErrorAlert("You don't have enough singa to perform this action");
        }
    }

    //Saca la lista de hechizos que sabe el mago
    private ArrayList<Spell> getWizardMemory() {
        ArrayList<Spell> learntSpells = new ArrayList<>();
        demiurgeHomeManager.getMemory().iterator().forEachRemaining(item -> {
            learntSpells.add((Spell) item);
        });
        return learntSpells;
    }

    //Saca la lista de hechizos que el mago puede aprender
    private ArrayList<Spell> getHomeLibrary() {
        ArrayList<Spell> unlearntSpells = new ArrayList<>();
        demiurgeHomeManager.getLibrary().iterator().forEachRemaining(item -> {
            unlearntSpells.add((Spell) item);
        });
        return unlearntSpells;
    }
}
