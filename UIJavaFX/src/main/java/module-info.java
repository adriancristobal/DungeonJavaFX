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
    exports game;
    exports game.demiurge;
    exports game.demiurge.exceptions;
    exports game.actions;
    exports game.character;
    exports game.character.exceptions;
    exports game.conditions;
    exports game.dungeon;
    exports game.object;
    exports game.objectContainer;
    exports game.objectContainer.exceptions;
    exports game.spell;
    exports game.spellContainer;
    exports game.util;
    exports console;
    exports loaderManual;

    opens ui.main;
    opens ui.common;
    opens ui.screens.menu;
    opens ui.screens.principal;
    opens ui.screens.dungeon;
    opens ui.screens.home;



}