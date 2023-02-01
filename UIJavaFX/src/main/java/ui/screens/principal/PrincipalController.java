package ui.screens.principal;

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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j2;
import ui.common.BaseScreenController;
import ui.common.Pantallas;
//import ui.common.ScreenConstants;
import game.demiurge.Demiurge;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

//@Log4j2
public class PrincipalController extends BaseScreenController implements Initializable {
    private Demiurge demiurge;

    public Demiurge getDemiurge() {
        return demiurge;
    }

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

    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
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
    }

    public void hideMenuItems() {
        generateImportMenuItem.setVisible(false);
        saveGameMenuItem.setVisible(false);
        exitToDesktopMenuItem.setVisible(true);
    }

    @FXML
    public void generateImport(ActionEvent actionEvent) {
    }

    @FXML
    public void saveGame(ActionEvent actionEvent) {
    }

    @FXML
    public void exitToDesktop(ActionEvent actionEvent) {
        closeWindowEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void goMainMenu() {
        cargarPantalla(Pantallas.MAIN_MENU);
        hideMenuItems();
        hideHud();
    }

    public void goHome() {
        cargarPantalla(Pantallas.HOME);
        showMenuItems();
    }

    public void goDungeon() {
        cargarPantalla(Pantallas.DUNGEON);
        showMenuItems();
    }


    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
//        alert.setTitle(ScreenConstants.QUIT_GAME);
//        alert.setContentText(ScreenConstants.CLOSE_GAME);
        alert.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();


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

        //singaAmountBotHud.setText(actualwizard.getCarriedSinga().toString());
        //crystalsAmountBotHud.setText(actualWizard.getCrystalCarrier().toString());
        //maxCrystalsAmountBotHud.setText(actualWizard.getCrystalCarrier().getMaxCapacity().toString());
        //lifeAmountBotHud.setText(actualWizard.getLife().toString());
        //maxLifeAmountBotHud.setText(actualWizard.getLifeMax().toString());
        //energyAmountBotHud.setText(actualWizard.getEnergy().toString());
        //maxEnergyAmountBotHud.setText(actualWizard.getEnergyMax().toString());

        //IMAGES
        imgCrystalBotHud.setImage(new Image(getClass().getResource("/images/crystal.png").toExternalForm()));
        imgEnergyBotHud.setImage(new Image(getClass().getResource("/images/energy.png").toExternalForm()));
        imgLifeBotHud.setImage(new Image(getClass().getResource("/images/life.png").toExternalForm()));
        imgNecklaceBotHud.setImage(new Image(getClass().getResource("/images/necklace.png").toExternalForm()));
        imgSingaBotHud.setImage(new Image(getClass().getResource("/images/singa.png").toExternalForm()));
        imgWeaponBotHud.setImage(new Image(getClass().getResource("/images/sword.png").toExternalForm()));
    }


}
