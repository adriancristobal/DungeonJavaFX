module UIJavaFX {
    requires jakarta.cdi;
    requires javafx.graphics;
    requires jakarta.inject;
    requires javafx.fxml;
    requires javafx.controls;
    requires lombok;

    exports ui.screens.menu;
    exports ui.common;
    exports ui.main;
    exports ui.screens.principal;
    exports ui.screens.dungeon;
    exports ui.screens.createDungeon;
    exports ui.screens.roomMonsterDungeonScreen;
    exports ui.screens.battleScreen;
    exports ui.screens.home;

    opens ui.main;
    opens ui.common;
    opens ui.screens.menu;
    opens ui.screens.principal;
    opens ui.screens.dungeon;
    opens ui.screens.roomMonsterDungeonScreen;
    opens ui.screens.battleScreen;
    opens ui.screens.home;



}