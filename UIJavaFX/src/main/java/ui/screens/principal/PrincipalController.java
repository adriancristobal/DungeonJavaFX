package ui.screens.principal;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j2;
import ui.common.BaseScreenController;
import ui.common.Pantallas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class PrincipalController implements Initializable {
    @FXML
    private Menu principalMenu;
    @FXML
    private MenuItem generateImportMenuItem;
    @FXML
    private MenuItem saveGameMenuItem;
    @FXML
    private MenuItem exitToDesktopMenuItem;

    Instance<Object> instance;
    private Alert alert;
    @FXML
    private BorderPane root;

    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
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
            log.error(e.getMessage(), e);
        }
        return panePantalla;
    }


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
    }

    public void goMainMenu() {
        cargarPantalla(Pantallas.MAIN_MENU);
        hideMenuItems();
    }

    public void goHome() {
        cargarPantalla(Pantallas.HOME);
        showMenuItems();
    }

    public void goDungeon() {
        cargarPantalla(Pantallas.DUNGEON);
        showMenuItems();
    }

    public void goMonsterDungeon(){
        cargarPantalla(Pantallas.MONSTER_DUNGEON);
        showMenuItems();
    }
}
