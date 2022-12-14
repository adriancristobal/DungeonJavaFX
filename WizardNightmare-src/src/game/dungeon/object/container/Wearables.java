package game.dungeon.object.container;

import game.character.exceptions.WizardNoWeaponException;
import game.dungeon.Domain;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Item;
import game.dungeon.object.item.Necklace;
import game.dungeon.object.item.Ring;
import game.dungeon.object.item.Weapon;

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
    }

    public void remove(Item si) {
        if (si instanceof Weapon)
            weapons--;
        else if (si instanceof Necklace)
            necklaces--;
        else if (si instanceof Ring)
            rings--;
        items.remove(si);
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

    public Weapon getWeapon() throws WizardNoWeaponException {
        for (Item s : items)
            if (s instanceof Weapon)
                return (Weapon) s;
        throw new WizardNoWeaponException();
    }

    public String toString() {
        String exit = "[";
        for (Item s : items)
            exit = exit.concat(s.toString() + " ");
        exit = exit.concat("]");
        return exit;
    }
}
