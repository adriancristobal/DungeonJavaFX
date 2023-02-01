package game.dungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class Dungeon {

    private final ArrayList<Room> rooms;

    public Dungeon() {
        rooms = new ArrayList<>();
    }

    public void addRoom(Room room) { rooms.add(room); }

    public Room getRoom(int index) { return rooms.get(index); }

    public Iterator<Room> iterator() { return rooms.iterator(); }

    public void generateCrystals(int maxElements, int maxCapacity) {
        for (Room r : rooms)
            r.generateCrystals(maxElements, maxCapacity);
    }
}
