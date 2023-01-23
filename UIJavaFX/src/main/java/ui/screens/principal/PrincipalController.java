package ui.screens.principal;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import ui.common.BaseScreenController;
import ui.screens.common.Pantallas;

import java.awt.*;
import java.io.IOException;

@Log4j2
public class PrincipalController {
    @FXML
    public BorderPane rootPantallaPrincipal;
    @FXML
    private MenuBar menuPrincipal;
    @FXML
    private MenuItem menuLogout;
    private Stage primaryStage;

    Instance<Object> instance;

    private final Alert alert;

    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert= new Alert(Alert.AlertType.NONE);
    }



    private void cambioPantalla(Pane pantallaNueva) {
        rootPantallaPrincipal.setCenter(pantallaNueva);
    }

    private void cargarPantalla(Pantallas pantalla) {
        cambioPantalla(cargarPantallaPane(pantalla.getRutaPantalla()));
    }

    public void initialize() {
        menuLogout.setVisible(false);
        cargarPantalla(Pantallas.LOGIN);
    }

    private Pane cargarPantallaPane(String ruta) {
        Pane panePantalla = null;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalLoaded();


        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return panePantalla;
    }

    public void onLoginHecho() {
        menuLogout.setVisible(true);
        cargarPantalla(Pantallas.WELCOME);
    }

    @FXML
    private void menuClick(ActionEvent actionEvent) {
//        switch (((MenuItem) actionEvent.getSource()).getId()) {
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
//        }

    }

    public void setStage (Stage stage){
        primaryStage = stage;
        //primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }
}
