package org.example;

import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;

import java.io.File;

public interface DungeonLoaderXML {

    void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File file);

    void save(Demiurge demiurge, DungeonConfiguration dungeonConfiguration, File file);

}
