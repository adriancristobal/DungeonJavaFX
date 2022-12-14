package game.dungeon.object.container;

import game.dungeon.Domain;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Item;
import game.dungeon.object.item.Necklace;
import game.dungeon.object.item.Ring;
import game.dungeon.object.item.Weapon;

public class Chest extends Container{

    public Chest(int c) { super(Domain.NONE, c); }

    /**
     * Adds rings, necklaces or weapon
     * @param i the item to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerUnacceptedItemException, ContainerFullException {
        if(full())
            throw new ContainerFullException();
        else if(i instanceof Necklace || i instanceof Ring || i instanceof Weapon) {
            items.add(i);
        }else
            throw new ContainerUnacceptedItemException();
    }

}
