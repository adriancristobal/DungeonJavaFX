package nightmare.dungeon;

public class Door {
    private Site a;
    private Site b;
    private boolean used = false;

    public Door(Site a, Site b){
        this.a = a;
        this.b = b;
        a.addDoor(this);
        b.addDoor(this);
    }

    private Site getOtherSite(Site current){
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

    public Site sniffFrom(Site current) {
        return getOtherSite(current);
    }
}
