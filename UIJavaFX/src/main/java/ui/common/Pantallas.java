package ui.common;

public enum Pantallas {
    MAIN_MENU("/fxml/mainMenuDungeon.fxml"),
    HOME("/fxml/homeDungeon.fxml"),
    STORAGE("/fxml/storageHome_dungeon.fxml"),
    DUNGEON("/fxml/roomDungeon_noMonster.fxml");


    private final String rutaPantalla;
    Pantallas(String ruta) {
        this.rutaPantalla=ruta;
    }
    public String getRutaPantalla(){return rutaPantalla;}

}
