package game.dungeon.structure;

import game.dungeon.object.container.CrystalFarm;
import game.dungeon.object.container.RoomSet;
import game.dungeon.object.item.SingaCrystal;
import game.character.Creature;

public class Room extends Site {
    private CrystalFarm farm;
    private Creature creature = null;

    public Room(int ID, String desc, RoomSet container) {
        super(ID, desc, container);
        farm = new CrystalFarm(0);
    }

    public Room(int ID, String desc, RoomSet container, boolean e) {
        super(ID, desc, container, e);
        farm = new CrystalFarm(0);
    }

    //Crystals
    public void generateCrystals(int maxElements, int maxCapacity) { farm.grow(maxElements, maxCapacity); }
    public int numberOfCrystals() { return farm.getValue(); }
    public SingaCrystal gather() { return farm.gather(); }


    //Creature
    public Creature getCreature() {
        return creature;
    }
    public void setCreature(Creature c) {
        creature = c;
    }
    public boolean isAlive(){ return creature.isAlive();}


    public String toString() {

        String exit = "ID(" + ID + ") Exit(" + this.exit + ") " + description;
        if (creature != null)
            exit = exit.concat("\n\tCreature: " + creature);
        exit = exit.concat("\n\tCrystalFarm[" + farm.toString() + "]");
        exit = exit.concat("\n\tItems" + container.toString());
        return exit;
    }


}
