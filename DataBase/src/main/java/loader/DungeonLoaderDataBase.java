package loader;

import game.demiurge.Demiurge;
import game.demiurge.DungeonConfiguration;
import game.dungeon.Home;
import game.objectContainer.Chest;
import game.spell.AirAttack;
import game.spell.ElectricAttack;
import game.spell.FireAttack;
import game.spellContainer.Knowledge;
import game.spellContainer.Library;

public class DungeonLoaderDataBase implements DungeonLoader{
    @Override
    public void load(Demiurge demiurge, DungeonConfiguration dungeonConfiguration) {

        Knowledge library = new Library();
        library.add(new ElectricAttack());
        library.add(new FireAttack());
        library.add(new AirAttack());
        Home home = new Home(Constants.HOME_NAME, Constants.INITIAL_COMFORT, Constants.INITIAL_SINGA, Constants.INITIAL_SINGA_CAPACITY, new Chest(Constants.INITIAL_CHEST_CAPACITY), library);


    }
}
