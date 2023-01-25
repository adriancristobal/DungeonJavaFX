package ui.screens.principal;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.extern.log4j.Log4j2;
import ui.common.BaseScreenController;
import ui.common.Pantallas;

import java.io.IOException;

@Log4j2
public class PrincipalController {
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

    public void initialize() {
        generateImportMenuItem.setVisible(false);
        saveGameMenuItem.setVisible(false);
        cargarPantalla(Pantallas.MAIN_MENU);
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

    //if we are in mainMenuDungeon.xml hide generateImportMenuItem and saveGameMenuItem

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


    @Deprecated
    private void menuClick(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
//            case "menuLogin" -> cargarPantalla(Pantallas.LOGIN);
//            case "menuLogout" -> cargarPantalla(Pantallas.LOGOUT);
//            case "menuRegister" -> cargarPantalla(Pantallas.REGISTER);
//            case "menuGetAllNewspaper" -> cargarPantalla(Pantallas.PANTALLA_GET_ALL_NEWSPAPER);
//            case "menuAddNewspaper" -> cargarPantalla(Pantallas.PANTALLA_ADD_NEWSPAPER);
//            case "menuDeleteNewspaper" -> cargarPantalla(Pantallas.PANTALLA_DELETE_NEWSPAPER);
//            case "menuUpdateNewspaper" -> cargarPantalla(Pantallas.PANTALLA_UPDATE_NEWSPAPER);
//            case "menuGetAllArticle" -> cargarPantalla(Pantallas.PANTALLA_GET_ALL_ARTICLE);
//            case "menuAddArticle" -> cargarPantalla(Pantallas.PANTALLA_ADD_ARTICLE);
//            case "menuDeleteArticle" -> cargarPantalla(Pantallas.PANTALLA_DELETE_ARTICLE);
//            case "menuUpdateArticle" -> cargarPantalla(Pantallas.PANTALLA_UPDATE_ARTICLE);
//            case "menuGetAllReader" -> cargarPantalla(Pantallas.PANTALLA_GET_ALL_READER);
//            case "menuAddReader" -> cargarPantalla(Pantallas.PANTALLA_ADD_READER);
//            case "menuDeleteReader" -> cargarPantalla(Pantallas.PANTALLA_DELETE_READER);
//            case "menuUpdateReader" -> cargarPantalla(Pantallas.PANTALLA_UPDATE_READER);
        }
        /*if ("menuGetAllNewspaper".equals(((MenuItem) actionEvent.getSource()).getId())) {
            cargarPantalla(Pantallas.PANTALLA_GET_ALL_NEWSPAPER);
        } else if ("menuGetAllArticle".equals(((MenuItem) actionEvent.getSource()).getId())) {
            cargarPantalla(Pantallas.PANTALLA_GET_ALL_ARTICLE);
        } else if ("menuAddArticle".equals(((MenuItem) actionEvent.getSource()).getId())) {
            cargarPantalla(Pantallas.PANTALLA_ADD_ARTICLE);
        }
         */
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
}
