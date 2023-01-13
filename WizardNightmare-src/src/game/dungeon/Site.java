package game.dungeon;

import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.object.Item;
import game.objectContainer.Container;

import java.util.ArrayList;
import java.util.Iterator;

public class Site {
    final int ID;
    final String description;
    boolean visited = false;
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
    public boolean isVisited() { return visited; }
    public void visit() { visited = true; }
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
    public Iterator<Door> iterator(){ return doors.iterator(); }

}
