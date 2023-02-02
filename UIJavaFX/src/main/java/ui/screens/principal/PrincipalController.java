package ui.screens.principal;

import game.DungeonLoaderXML;
import game.character.Wizard;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Room;
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
import ui.common.BaseScreenController;
import ui.common.Pantallas;
//import ui.common.ScreenConstants;
import game.demiurge.Demiurge;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//@Log4j2
public class PrincipalController extends BaseScreenController implements Initializable {

    private final DungeonLoaderXML loader;

    private Demiurge demiurge;
    private int currentRoomId;
    public int getRoomId(){
        return currentRoomId;
    }

    @FXML
    public MenuItem goToMainMenu;
    @FXML
    public Pane characterBotHud;
    @FXML
    public Text singaAmountBotHud;
    @FXML
    public Text crystalsAmountBotHud;
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
    private ImageView imgCrystalBotHud;
    @FXML
    private ImageView imgEnergyBotHud;

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
    @FXML
    private MenuItem exitToDesktopMenuItem;
    //private Wizard actualWizard;
    Instance<Object> instance;
    private Stage primaryStage;
    private Alert alert;
    @FXML
    private BorderPane root;
    public Wizard actualWizard;
    public Room actualRoom;

    public Demiurge getDemiurge() {
        return demiurge;
    }

    //ESTE SET SOLO SE USA AL CARGAR LA MAZMORRA DEL XML O LA BASE DE DATOS
    public void setDemiurge(Demiurge loadedDemiurge){
        demiurge = loadedDemiurge;
        getWizard();
        this.goHome();
    }

    public void getWizard() {
        actualWizard = this.demiurge.getWizard();
    }

    @Inject
    public PrincipalController(Instance<Object> instance, DungeonLoaderXML loader) {
        this.loader = loader;
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
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
        fillHud();
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
        exitToDesktopMenuItem.setVisible(true);
        goToMainMenu.setVisible(true);
    }

    public void hideMenuItems() {
        generateImportMenuItem.setVisible(false);
        saveGameMenuItem.setVisible(false);
        exitToDesktopMenuItem.setVisible(true);
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

    @FXML
    public void generateImport(ActionEvent actionEvent) {

    }

    @FXML
    public void saveGame(ActionEvent actionEvent) {
        try {
            DungeonConfiguration config = new DungeonConfiguration();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game (.xml)");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            loader.save(this.demiurge, config, file);
        } catch (Exception e){
            e.printStackTrace();
            showErrorAlert("Error reading file");
        }
    }

    @FXML
    public void exitToDesktop(ActionEvent actionEvent) {

    }

    @FXML
    public void goToHome(ActionEvent actionEvent) {
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

    public void goToRoom(int roomId) {
        currentRoomId = roomId;
        showMenuItems();
        fillHud();
        revealHud();
        cargarPantalla(Pantallas.DUNGEON);
    }


    private void closeWindowEvent(WindowEvent event) {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        alertDialog.getButtonTypes().remove(ButtonType.OK);
        alertDialog.getButtonTypes().add(ButtonType.CANCEL);
        alertDialog.getButtonTypes().add(ButtonType.YES);
//        alert.setTitle(ScreenConstants.QUIT_GAME);
//        alert.setContentText(ScreenConstants.CLOSE_GAME);
        alertDialog.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alertDialog.showAndWait();


        res.ifPresent(buttonType -> {
            if (buttonType == ButtonType.CANCEL) {
                event.consume();
            }
        });
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

        if(actualWizard!=null) {
            crystalsAmountBotHud.setText(String.valueOf(actualWizard.getCrystalCarrier().getValue()));
            maxCrystalsAmountBotHud.setText(String.valueOf(actualWizard.getCrystalCarrier().getMaximum()));
            lifeAmountBotHud.setText(String.valueOf(actualWizard.getLife()));
            maxLifeAmountBotHud.setText(String.valueOf(actualWizard.getLifeMax()));
            energyAmountBotHud.setText(String.valueOf(actualWizard.getEnergy()));
            maxEnergyAmountBotHud.setText(String.valueOf(actualWizard.getEnergyMax()));
        }

        //IMAGES
        imgCrystalBotHud.setImage(new Image(getClass().getResource("/images/crystal.png").toExternalForm()));
        imgEnergyBotHud.setImage(new Image(getClass().getResource("/images/energy.png").toExternalForm()));
        imgLifeBotHud.setImage(new Image(getClass().getResource("/images/life.png").toExternalForm()));
        imgNecklaceBotHud.setImage(new Image(getClass().getResource("/images/necklace.png").toExternalForm()));
        imgWeaponBotHud.setImage(new Image(getClass().getResource("/images/sword.png").toExternalForm()));
    }
}
