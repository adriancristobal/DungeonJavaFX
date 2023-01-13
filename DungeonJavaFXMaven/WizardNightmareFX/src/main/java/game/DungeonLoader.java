package game;

import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;

public interface DungeonLoader {

    public void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration);

}
