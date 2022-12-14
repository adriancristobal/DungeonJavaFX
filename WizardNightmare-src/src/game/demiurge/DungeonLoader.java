package game.demiurge;

import game.character.Wizard;
import game.dungeon.structure.Dungeon;
import game.dungeon.structure.Home;

public interface DungeonLoader {

    public void load(Demiurge demiurge);

    public void home(Home home);
    public void dungeon(Dungeon dungeon);
    public void wizard(Wizard wizard);
}
