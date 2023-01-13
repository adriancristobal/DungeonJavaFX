package game.objectContainer;

import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.object.Item;
import game.object.SingaCrystal;

/**
 * A portable storage to carry the gathered crystals till arrive to home. Maybe a bracelet or a bag...
 * The capacity is limited.
 */
public class CrystalFarm extends ContainerSinga {

    public CrystalFarm(int c) { super(c); }

    /**
     * Adds just a crystal
     * @param i the crystal to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerUnacceptedItemException, ContainerFullException {
        if(isFull())
            throw new ContainerFullException();
        else if(i instanceof SingaCrystal) {
            items.add(i);
        }else
            throw new ContainerUnacceptedItemException();
    }

    public void grow(int maxElements, int maxCapacity) {
        items.clear();
        updateValue((int) (Math.random() * maxElements + 1));
        for (int i = 0; i < getValue(); i++)
            items.add(SingaCrystal.createCrystal(maxCapacity));
    }

    public SingaCrystal gather() { return (SingaCrystal) items.remove(0); }

}
