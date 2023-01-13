package game.objectContainer;

import game.Domain;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.object.Item;
import game.object.Necklace;
import game.object.Ring;
import game.object.Weapon;

public class Chest extends Container{

    public Chest(int c) { super(Domain.NONE, c); }

    /**
     * Adds rings, necklaces or weapon
     * @param i the item to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerUnacceptedItemException, ContainerFullException {
        if(isFull())
            throw new ContainerFullException();
        else if(i instanceof Necklace || i instanceof Ring || i instanceof Weapon) {
            items.add(i);
        }else
            throw new ContainerUnacceptedItemException();
    }


}
