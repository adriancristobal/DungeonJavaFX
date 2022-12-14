package game.dungeon.object.container;

import game.dungeon.Domain;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.*;

public class JewelryBag extends Container{


    public JewelryBag(int c) { super(Domain.NONE, c); }

    /**
     * Adds just a ring or a necklace
     * @param i the item to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerUnacceptedItemException, ContainerFullException {
        if(full())
            throw new ContainerFullException();
        else if(i instanceof Necklace || i instanceof Ring) {
            items.add(i);
        }else
            throw new ContainerUnacceptedItemException();
    }

}
