package game.dungeon.object.container;

import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Item;
import game.dungeon.object.item.SingaCrystal;

/**
 * A portable storage to carry the gathered crystals till arrive to home. Maybe a bracelet or a bag...
 * The capacity is limited.
 */
public class CrystalCarrier extends ContainerSinga {

    public CrystalCarrier(int c) { super(c); }

    /**
     * Adds just a crystal
     * @param i the crystal to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerUnacceptedItemException, ContainerFullException {
        if(full())
            throw new ContainerFullException();
        else if(i instanceof SingaCrystal) {
            items.add(i);
        }else
            throw new ContainerUnacceptedItemException();
    }

}
