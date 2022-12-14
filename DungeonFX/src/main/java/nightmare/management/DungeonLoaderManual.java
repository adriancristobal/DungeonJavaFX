package nightmare.management;

import nightmare.pj.CrystalCarrier;
import nightmare.pj.Wereables;
import nightmare.pj.WereablesFullException;
import nightmare.pj.Wizard;
import nightmare.dungeon.*;
import nightmare.items.*;
import nightmare.pnj.Creature;
import nightmare.spells.Magic_Type;

import static nightmare.management.ConfigValues.*;

public class DungeonLoaderManual implements DungeonLoader {

    @Override
    public void load(Demiurge demiurge){
        System.out.println("\tCreating HOME");
        Home home = new Home(INITIAL_COMFORT, INITIAL_SINGA, INITIAL_SINGA_CAPACITY, INITIAL_STORAGE_CAPACITY);
        home.getStorage().add(new Weapon(2));
        demiurge.setHome(home);

        System.out.println("\tCreating DUNGEON");
        Dungeon dungeon = new Dungeon();
        Room room;
        int id = 0;

        System.out.println("\tCreating ROOMS");
        room = new Room(id++, "Common room connected with HOME");
        room.addItem(new Necklace(Magic_Type.LIFE, 5));
        dungeon.getRooms().add(room);

        room = new Room(id++, "Common room in the middle");
        room.setCreature(new Creature(Magic_Type.ELECTRICITY, 5, 1));
        dungeon.getRooms().add(room);

        room = new Room(id++, "Common room and Exit", true);
        dungeon.getRooms().add(room);

        System.out.println("\tCreating DOORS");
        new Door(home, dungeon.getRooms().get(0));
        new Door(dungeon.getRooms().get(0), dungeon.getRooms().get(1));
        new Door(dungeon.getRooms().get(1), dungeon.getRooms().get(2));

        System.out.println("\t\tTotal rooms in dungeon: " + id);
        demiurge.setDungeon(dungeon);

        System.out.println("\tAdding WIZARD to the system.");
        Wizard wizard = new Wizard("Nobody", INITIAL_LIFE, INITIAL_LIFE_MAX, INITIAL_ENERGY, INITIAL_ENERGY_MAX,
                new CrystalCarrier(INITIAL_CRYSTAL_CARRIER_CAPACITY),
                new Wereables(INITIAL_MAX_WEAPONS, INITIAL_MAX_NECKLACES, INITIAL_MAX_RINGS));
        wizard.getCrystalCarrier().add(new Crystal(5));
        try {
            wizard.addItem(new Weapon(1));
        } catch (WereablesFullException ignored) {
        }
        demiurge.setWizard(wizard);

    }
}
