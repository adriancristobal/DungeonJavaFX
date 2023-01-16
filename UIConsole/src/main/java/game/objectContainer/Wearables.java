package game.objectContainer;

import game.Domain;
import game.object.Item;
import game.object.Necklace;
import game.object.Ring;
import game.object.Weapon;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;

public class Wearables extends Container{

    private int weapons, necklaces, rings;
    private final int weaponsMAX;
    private final int necklacesMAX;
    private final int ringsMAX;

    public Wearables(int w, int n, int r) {
        super(Domain.NONE, w+n+r);
        weapons = necklaces = rings = 0;
        weaponsMAX = w;
        necklacesMAX = n;
        ringsMAX = r;
    }

    public void add(Item si) throws ContainerFullException, ContainerUnacceptedItemException {
        if (si instanceof Weapon)
            addWeapon((Weapon) si);
        else if (si instanceof Necklace)
            addNecklace((Necklace) si);
        else if (si instanceof Ring)
            addRing((Ring) si);
        else
            throw new ContainerUnacceptedItemException();
        si.view();
    }

    public Item remove(int index) {
        Item item = items.get(index);

        if (item instanceof Weapon)
            weapons--;
        else if (item instanceof Necklace)
            necklaces--;
        else if (item instanceof Ring)
            rings--;

        return items.remove(index);
    }

    public void exchangeWearable(Item o, Item n) throws ContainerUnacceptedItemException {
        if (o.getClass() == n.getClass()) {
            items.remove(o);
            items.add(n);
        } else
            throw new ContainerUnacceptedItemException();
    }

    private void addWeapon(Weapon w) throws ContainerFullException {
        if (weapons < weaponsMAX) {
            weapons++;
            items.add(w);
        } else
            throw new ContainerFullException();
    }

    private void addNecklace(Necklace n) throws ContainerFullException {
        if (necklaces < necklacesMAX) {
            necklaces++;
            items.add(n);
        } else
            throw new ContainerFullException();
    }

    private void addRing(Ring r) throws ContainerFullException {
        if (rings < ringsMAX) {
            rings++;
            items.add(r);
        } else
            throw new ContainerFullException();
    }

    public Weapon getWeapon() {
        for (Item s : items)
            if (s instanceof Weapon)
                return (Weapon) s;
        return null;
    }

    public int getRingProtection(Domain type){
        int value = -1;
        for (Item s : items)
            if (s instanceof Ring) {
                Ring r = (Ring) s;
                if (r.getDomain() == type && r.getValue() > value)
                    value = r.getValue();
            }
        return value;
    }

}
