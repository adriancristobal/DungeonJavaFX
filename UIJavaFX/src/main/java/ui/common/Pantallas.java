package ui.common;

public enum Pantallas {
    MAIN_MENU("/fxml/mainMenuDungeon.fxml"),
    HOME("/fxml/homeDungeon.fxml"),
    DUNGEON("/fxml/roomDungeon_noMonsterNew.PNG.fxml"),
    MONSTER_DUNGEON("/fxml/roomMonsterDungeon.fxml");

    private final String rutaPantalla;
    Pantallas(String ruta) {
        this.rutaPantalla=ruta;
    }
    public String getRutaPantalla(){return rutaPantalla;}

}
