package nightmare.dungeon;

import java.util.ArrayList;

public class Dungeon {

    private ArrayList<Room> rooms;

    public Dungeon(){
        rooms = new ArrayList<>();
    }

    public ArrayList<Room>getRooms(){
        return rooms;
    }

    public void generateCrystals(int maxElements, int maxCapacity){
        for (Room r: rooms)
            r.generateCrystals(maxElements, maxCapacity);
    }
}
