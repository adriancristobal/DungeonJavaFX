package game.dungeon;

import game.character.Creature;
import game.object.SingaCrystal;
import game.objectContainer.CrystalFarm;
import game.objectContainer.RoomSet;

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
    public boolean isEmpty() { return farm.isEmpty(); }
    public SingaCrystal gather() { return farm.gather(); }


    //Creature
    public Creature getCreature() {
        return creature;
    }
    public void setCreature(Creature c) {
        creature = c;
    }
    public boolean isAlive(){
        if(creature == null)
            return false;
        return creature.isAlive();
    }


    public String toString() {

        String exit = "ID(" + ID + ") Exit(" + this.exit + ") " + description;
        if (creature != null)
            exit = exit.concat("\n\tCreature: " + creature);
        exit = exit.concat("\n\tCrystalFarm[" + farm.toString() + "]");
        exit = exit.concat("\n\tItems" + container.toString());
        return exit;
    }


}
