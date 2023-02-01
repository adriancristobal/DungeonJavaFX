package game.dungeon;

public class Door {
    private final Site a;
    private final Site b;
    private boolean used = false;

    public Door(Site a, Site b) {
        this.a = a;
        this.b = b;
        a.addDoor(this);
        b.addDoor(this);
    }

    private Site getOtherSite(Site current) {
        if (a == current)
            return b;
        else
            return a;
    }

    public boolean isUsed() {
        return used;
    }

    public Site openFrom(Site current) {
        used = true;
        return getOtherSite(current);
    }

    public Site showFrom(Site current) {
        return getOtherSite(current);
    }

    public String toString(){
        return a.getID() + ":" + b.getID();
    }
}
