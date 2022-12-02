package nightmare.dungeon;

import java.util.ArrayList;

public class Site {

    private ArrayList<Door> doors;

    public Site(){
        doors = new ArrayList<>();
    }

    public int numberOfDoors(){
        return doors.size();
    }

    public void addDoor(Door p){
        doors.add(p);
    }

    public Site useDoor(int index){
        return doors.get(index).openFrom(this);
    }

    public Site sniffDoor(int index){
        return doors.get(index).sniffFrom(this);
    }

    public ArrayList<Door> getDoors(){ return doors; }

    public Door doorAt(int index){
        return doors.get(index);
    }

    public Site useDoor(Door p){
        return p.openFrom(this);
    }

}
