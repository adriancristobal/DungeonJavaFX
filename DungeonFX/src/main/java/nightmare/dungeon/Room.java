package nightmare.dungeon;

import nightmare.pnj.Creature;
import nightmare.items.Crystal;
import nightmare.items.StorableItem;

import java.util.ArrayList;

public class Room extends Site {
    private int ID;
    private boolean exit = false;
    private String description;

    private ArrayList<Crystal> crystals;
    private ArrayList<StorableItem> items;
    private Creature creature = null;

    public Room(int ID, String desc){
        super();
        this.ID = ID;
        description = desc;
        crystals = new ArrayList<>();
        items = new ArrayList<>();
    }

    public Room(int ID, String desc, boolean e){
        super();
        this.ID = ID;
        description = desc;
        crystals = new ArrayList<>();
        items = new ArrayList<>();
        exit = e;
    }

    public int getID(){
        return ID;
    }

    public String getDescription(){
        return description;
    }

    public boolean isExit(){
        return exit;
    }
    
    //ABOUT CRYSTALS
    public void generateCrystals(int maxElements, int maxCapacity){
        crystals.clear();
        int max = (int)(Math.random()* maxElements)+1;
        for(int i = 0; i<max; i++)
            crystals.add(Crystal.createCrystal(maxCapacity));
    }

    public int numberOfCrystals(){
        return crystals.size();
    }

    public Crystal gather(){
        return crystals.remove(0);
    }

    //ABOUT Items
    public ArrayList<StorableItem> getItems() {
        return items;
    }

    public void addItem(StorableItem s){
        items.add(s);
    }

    public void removeItem (StorableItem s){
        items.remove(s);
    }

    //ABOUT Creature
    public Creature getCreature(){
        return creature;
    }
    public void setCreature(Creature c){
        creature = c;
    }

    public String toString(){
        int number = 0;
        int singa = 0;
        for(Crystal c: crystals){
            number++;
            singa += c.getSinga();
        }
        String exit = "ID(" + ID + ") Exit(" + this.exit + ") " + description;
        if(creature != null)
            exit = exit.concat("\n\tCreature: "+ creature.toString());
        exit = exit.concat("\n\tCrystals("+number+":"+singa+")");
        exit = exit.concat("\n\tItems [");
        for(StorableItem s: items)
            exit = exit.concat(s.toString()+ " ");
        exit = exit.concat("]");
        return exit;
    }

}
