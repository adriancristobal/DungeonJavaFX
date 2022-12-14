package game.dungeon.structure;

import game.dungeon.object.container.Container;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Item;

import java.util.ArrayList;
import java.util.Iterator;

public class Site {
    final int ID;
    final String description;
    boolean exit = false;
    final Container container;
    private final ArrayList<Door> doors;

    public Site(int ID, String description, Container container) {
        this.ID = ID;
        this.description = description;
        this.container = container;
        doors = new ArrayList<>();
    }

    public Site(int ID, String description, Container container, boolean exit) {
        this(ID, description, container);
        this.exit = exit;
    }

    public int getID() {
        return ID;
    }
    public String getDescription() {
        return description;
    }
    public boolean isExit() {
        return exit;
    }


    //Container
    public Container getContainer() { return container;}
    public void addItem(Item s) throws ContainerUnacceptedItemException, ContainerFullException { container.add(s); }


    //Doors
    public int getNumberOfDoors() {
        return doors.size();
    }
    public void addDoor(Door p) {
        doors.add(p);
    }
    public Site openDoor(int index) {
        return doors.get(index).openFrom(this);
    }
    public Iterator iterator(){ return doors.iterator(); }

}
