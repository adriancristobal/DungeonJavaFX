package ui.screens.common;

public enum Pantallas {

    LOGIN("/fxml/fxmlAccount/FXMLLogin.fxml"),
    REGISTER("/fxml/fxmlAccount/FXMLRegister.fxml"),
    LOGOUT("/fxml/fxmlAccount/FXMLLogout.fxml"),

    WELCOME ("/fxml/FXMLWelcom.fxml"),
    PANTALLA_GET_ALL_NEWSPAPER("/fxml/fxmlNewspaper/FXMLGetAllNewspaper.fxml"),
    PANTALLA_ADD_NEWSPAPER("/fxml/fxmlNewspaper/FXMLAddNewspaper.fxml"),
    PANTALLA_DELETE_NEWSPAPER("/fxml/fxmlNewspaper/FXMLDeleteNewspaper.fxml"),
    PANTALLA_UPDATE_NEWSPAPER("/fxml/fxmlNewspaper/FXMLUpdateNewspaper.fxml"),
    PANTALLA_GET_ALL_ARTICLE("/fxml/fxmlArticle/FXMLGetAllArticle.fxml"),
    PANTALLA_ADD_ARTICLE("/fxml/fxmlArticle/FXMLAddArticle.fxml"),
    PANTALLA_DELETE_ARTICLE("/fxml/fxmlArticle/FXMLDeleteArticle.fxml"),
    PANTALLA_UPDATE_ARTICLE("/fxml/fxmlArticle/FXMLUpdateArticle.fxml"),
    PANTALLA_GET_ALL_READER("/fxml/fxmlReader/FXMLGetAllReader.fxml"),
    PANTALLA_ADD_READER("/fxml/fxmlReader/FXMLAddReader.fxml"),
    PANTALLA_DELETE_READER("/fxml/fxmlReader/FXMLDeleteReader.fxml"),
    PANTALLA_UPDATE_READER("/fxml/fxmlReader/FXMLUpdateReader.fxml");
    private final String rutaPantalla;
    Pantallas(String ruta) {
        this.rutaPantalla=ruta;
    }
    public String getRutaPantalla(){return rutaPantalla;}


}
