package game.dungeon.object.container;

import game.dungeon.Domain;
import game.dungeon.object.item.Item;

public abstract class ContainerSinga extends Container {

    public ContainerSinga(int c) { super(Domain.NONE, c); }

    public int singa(){
        int singa = 0;
        for (Item i : items)
            singa += i.getValue();
        return singa;
    }

    public String toString() {
        return "(" + getValue() + ":" + singa() + ")";
    }

}
