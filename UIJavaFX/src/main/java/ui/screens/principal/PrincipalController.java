package ui.screens.principal;

import game.DungeonLoaderXML;
import game.character.Wizard;
import game.character.exceptions.WizardTiredException;
import game.demiurge.DemiurgeContainerManager;
import game.demiurge.DemiurgeDungeonManager;
import game.demiurge.DungeonConfiguration;
import game.demiurge.exceptions.EndGameException;
import game.demiurge.exceptions.GoHomekException;
import game.dungeon.Room;
import game.object.ItemCreationErrorException;
import game.object.Necklace;
import game.object.Ring;
import game.object.Weapon;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.SpellUnknowableException;
import game.util.ValueOverMaxException;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import ui.common.BaseScreenController;
import ui.common.Pantallas;
//import ui.common.ScreenConstants;
import game.demiurge.Demiurge;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

//@Log4j2
@Getter
@Setter
public class PrincipalController extends BaseScreenController implements Initializable {

    private final DungeonLoaderXML loader;
    private final DungeonConfiguration config;

    private Demiurge demiurge;
    private DemiurgeDungeonManager dungeonManager;
    public DemiurgeDungeonManager getDungeonManager(){
        return dungeonManager;
    }

    private DemiurgeContainerManager manager;



    @Inject
    public PrincipalController(Instance<Object> instance, DungeonLoaderXML loader) {
        this.loader = loader;
        this.instance = instance;
        this.demiurge = new Demiurge();
        this.config = new DungeonConfiguration();
        alert = new Alert(Alert.AlertType.NONE);
    }

    public DemiurgeContainerManager getManager() {
        return manager;
    }

    private int currentRoomId;

    public int getRoomId() {
        return currentRoomId;
    }

    @FXML
    public MenuItem goToMainMenu;
    @FXML
    public Pane characterBotHud;
    @FXML
    public Text dayInfoBotHud;
    @FXML
    public Text singaAmountBotHud;
    @FXML
    public Text crystalsAmountBotHud;
    @FXML
    public Text ringInfoBotHud;
    @FXML
    public Text ringInfoBotHudTwo;
    @FXML
    public Text weaponInfoBotHud;
    @FXML
    public Text necklaceInfoBotHud;
    @FXML
    public Text maxCrystalsAmountBotHud;
    @FXML
    public Text lifeAmountBotHud;
    @FXML
    public Text maxLifeAmountBotHud;
    @FXML
    public Text energyAmountBotHud;
    @FXML
    public Text maxEnergyAmountBotHud;
    @FXML
    public ImageView imgDayBotHud;
    @FXML
    private ImageView imgCrystalBotHud;
    @FXML
    private ImageView imgEnergyBotHud;
    @FXML
    public ImageView imgRingBotHud;
    @FXML
    public ImageView imgRingBotHudTwo;

    @FXML
    private ImageView imgLifeBotHud;

    @FXML
    private ImageView imgNecklaceBotHud;

    @FXML
    private ImageView imgSingaBotHud;

    @FXML
    private ImageView imgWeaponBotHud;
    @FXML
    private Menu principalMenu;
    @FXML
    private MenuItem generateImportMenuItem;
    @FXML
    private MenuItem saveGameMenuItem;
    Instance<Object> instance;
    private Stage primaryStage;
    private Alert alert;
    @FXML
    private BorderPane root;
    public Wizard currentWizard;

    public Room currentRoom;

    public void loadManagers(File file) {
        demiurge.loadEnvironment((demiurge, dungeonConfiguration) -> {
            try {
                loader.load(demiurge, dungeonConfiguration, file);
                currentWizard = demiurge.getWizard();
                manager = new DemiurgeContainerManager(currentWizard.getWearables(), currentWizard.getJewelryBag(), demiurge.getHome().getContainer());
                dungeonManager = new DemiurgeDungeonManager(dungeonConfiguration, demiurge.getWizard(), demiurge.getHome(), manager, demiurge.getEndChecker());
                goHome();
            } catch (Exception | ContainerUnacceptedItemException | SpellUnknowableException |
                     ValueOverMaxException |
                     ContainerFullException | ItemCreationErrorException e) {
                showErrorAlert("Error al cargar el XML");
            }
        });
    }

    public Demiurge getDemiurge() {
        return demiurge;
    }

    public void setDemiurge(Demiurge demiurge) {
        this.demiurge = demiurge;
        getWizard();
    }

    public void getWizard() {
        currentWizard = this.demiurge.getWizard();
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    private void cambioPantalla(Pane pantallaNueva) {
        root.setCenter(pantallaNueva);
    }

    private void cargarPantalla(Pantallas pantalla) {
        cambioPantalla(cargarPantallaPane(pantalla.getRutaPantalla()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goMainMenu();
    }

    private Pane cargarPantallaPane(String ruta) {
        Pane panePantalla = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalCargado();
        } catch (IOException e) {
            throw new RuntimeException(e);
//            log.error("Error al cargar la pantalla: " + ruta, e);
        }
        return panePantalla;
    }

    //TOP OPTIONS MENU
    public void showMenuItems() {
        generateImportMenuItem.setVisible(true);
        saveGameMenuItem.setVisible(true);
        goToMainMenu.setVisible(true);
    }

    public void hideMenuItems() {
        generateImportMenuItem.setVisible(false);
        saveGameMenuItem.setVisible(false);
        goToMainMenu.setVisible(false);
    }

    public void showErrorAlert(String message) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoAlert(String message) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showWarningAlert(String message) {
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void generateImport(ActionEvent actionEvent) {

    }

    @FXML
    public void saveGame(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game (.xml)");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            loader.save(this.demiurge, config, file);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error reading file");
        }
    }

    @FXML
    public void exitToDesktop(ActionEvent actionEvent) {

    }

    public void tired() {
        demiurge.nextDay();
        goHome();
        refreshDay();
    }

    @FXML
    public void goToMainMenu(ActionEvent actionEvent) {
        goMainMenu();
    }

    public void goMainMenu() {
        cargarPantalla(Pantallas.MAIN_MENU);
        hideMenuItems();
        hideHud();
    }

    public void goHome() {
        cargarPantalla(Pantallas.HOME);
        showMenuItems();
        fillHud();
        revealHud();
    }

    public void goStorage() {
        cargarPantalla(Pantallas.STORAGE);
        showMenuItems();
        hideHud();
    }

    //TODO: puede que la roomId no haga falta
    public void goToRoom(int roomId) {
        try {
            currentRoomId = roomId;
            showMenuItems();
            fillHud();
            revealHud();
            dungeonManager.openDoor(roomId);
            cargarPantalla(Pantallas.DUNGEON);
        } catch (WizardTiredException e) {
            showErrorAlert("You don't have enough energy.\nGoing to sleep");
        } catch (GoHomekException e) {
            showErrorAlert("You are already home");
        } catch (EndGameException e) {
            showInfoAlert("YOU WON!");
        } catch (Exception e){
            showErrorAlert(e.getMessage());
        }
    }

    //BOTTOM CHARACTER HUD
    public void hideHud() {
        characterBotHud.setVisible(false);
    }

    public void revealHud() {
        characterBotHud.setVisible(true);
    }

    //FILL + REFRESH HUD
    public void fillHud() {
        //TEXT
        fillTexts();

        //IMAGES
        imgCrystalBotHud.setImage(new Image(getClass().getResource("/images/crystal.png").toExternalForm()));
        imgEnergyBotHud.setImage(new Image(getClass().getResource("/images/energy.png").toExternalForm()));
        imgLifeBotHud.setImage(new Image(getClass().getResource("/images/life.png").toExternalForm()));
        imgNecklaceBotHud.setImage(new Image(getClass().getResource("/images/necklace.png").toExternalForm()));
        imgWeaponBotHud.setImage(new Image(getClass().getResource("/images/sword.png").toExternalForm()));
        imgRingBotHud.setImage(new Image(getClass().getResource("/images/ring.png").toExternalForm()));
        imgRingBotHudTwo.setImage(new Image(getClass().getResource("/images/ring.png").toExternalForm()));
        imgDayBotHud.setImage(new Image(getClass().getResource("/images/calendar.png").toExternalForm()));
    }

    public void refreshDay() {
        dayInfoBotHud.setText(String.valueOf(this.demiurge.getDay()));
        fillTexts();
    }

    public void refreshCrystalsHud() {
        crystalsAmountBotHud.setText(String.valueOf(currentWizard.getCrystalCarrier().size()));
    }

    public void fillTexts() {
        if (currentWizard != null) {
            dayInfoBotHud.setText(String.valueOf(this.demiurge.getDay()));
            crystalsAmountBotHud.setText(String.valueOf(currentWizard.getCrystalCarrier().size()));
            maxCrystalsAmountBotHud.setText(String.valueOf(currentWizard.getCrystalCarrier().getMaximum()));
            lifeAmountBotHud.setText(String.valueOf(currentWizard.getLife()));
            maxLifeAmountBotHud.setText(String.valueOf(currentWizard.getLifeMax()));
            energyAmountBotHud.setText(String.valueOf(currentWizard.getEnergy()));
            maxEnergyAmountBotHud.setText(String.valueOf(currentWizard.getEnergyMax()));
            setWearables();
        }
    }

    public void setWearables() {
        setHUDWeapon();
        setHUDNecklace();
        setHUDRings();
    }

    public void setHUDWeapon() {
        weaponInfoBotHud.setText("none");
        if (demiurge != null && currentWizard != null) {
            currentWizard.getWearables().iterator().forEachRemaining(item -> {
                if (item instanceof Weapon) {
                    weaponInfoBotHud.setText("Level " + ((Weapon) item).getValue());
                }
            });
        }
    }

    public void setHUDNecklace() {
        necklaceInfoBotHud.setText("none");
        if (demiurge != null && currentWizard != null) {
            currentWizard.getWearables().iterator().forEachRemaining(item -> {
                if (item instanceof Necklace) {
                    //TODO: añadir tipo de collar
                    necklaceInfoBotHud.setText("Level " + ((Necklace) item).getValue());
                }
            });
        }
    }

    public void setHUDRings() {
        ringInfoBotHud.setText("none");
        ringInfoBotHudTwo.setText("none");
        if (demiurge != null && currentWizard != null) {
            List<Ring> rings = new ArrayList<>();
            currentWizard.getWearables().iterator().forEachRemaining(item -> {
                if (item instanceof Ring) {
                    rings.add((Ring) item);
                }
            });

            //TODO: añadir tipo de anillos
            if (!rings.isEmpty()) {
                ringInfoBotHud.setText("Level " + rings.get(0).getValue());
            }
            if (rings.size() >= 2) {
                ringInfoBotHudTwo.setText("Level " + rings.get(1).getValue());
            }
        }
    }
}
