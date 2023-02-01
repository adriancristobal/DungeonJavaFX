package game.conditions;

import game.dungeon.Dungeon;
import game.dungeon.Room;

import java.util.Iterator;

public class VisitAllRoomsCondition implements Condition{
    Dungeon dungeon;

    public VisitAllRoomsCondition(Dungeon dungeon){ this.dungeon = dungeon; }

    @Override
    public boolean check() {
        Iterator it = dungeon.iterator();
        while (it.hasNext()){
            Room room = (Room) it.next();
            if(!room.isVisited())
                return false;
        }
        return true;
    }
}
