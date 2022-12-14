package nightmare.pj;

import nightmare.items.Necklace;
import nightmare.items.Ring;
import nightmare.items.StorableItem;
import nightmare.items.Weapon;

import java.util.ArrayList;

public class Wereables {

    private ArrayList<StorableItem> items;

    private int weapons, necklaces, rings;
    private int weaponsMAX, necklacesMAX, ringsMAX;

    public Wereables(int w, int n, int r){
        items = new ArrayList<>();
        weaponsMAX = w;
        necklacesMAX = n;
        ringsMAX = r;
        weapons = necklaces = rings = 0;
    }

    public ArrayList<StorableItem> getItems() {
        return items;
    }

    public void addWereable(StorableItem si) throws WereablesFullException {
        if(si instanceof Weapon)
            addWeapon((Weapon)si);
        else if(si instanceof Necklace)
            addNecklace((Necklace)si);
        else
            addRing((Ring)si);
    }

    public void removeWereable(StorableItem si) {
        if (si instanceof Weapon)
            weapons--;
        else if (si instanceof Necklace)
            necklaces--;
        else
            rings--;
        items.remove(si);
    }
    public void exchangeWereable(StorableItem o, StorableItem n) throws WereableIncompatibleException {
        if (o.getClass() == n.getClass()){
            items.remove(o);
            items.add(n);
        }else
            throw new WereableIncompatibleException();
    }

    private void addWeapon(Weapon w) throws WereablesFullException {
        if (weapons < weaponsMAX){
            weapons++;
            items.add(w);
        }else
            throw new WereablesFullException();
    }

    private void addNecklace(Necklace n) throws WereablesFullException {
        if (necklaces < necklacesMAX){
            necklaces++;
            items.add(n);
        }else
            throw new WereablesFullException();
    }

    private void addRing(Ring r) throws WereablesFullException {
        if (rings < ringsMAX){
            rings++;
            items.add(r);
        }else
            throw new WereablesFullException();
    }

    public Weapon getWeapon() throws WizardNoWeaponException {
        for(StorableItem s: items)
            if(s instanceof Weapon)
                return (Weapon)s;
        throw new WizardNoWeaponException();
    }

    public String toString(){
        String exit = new String("[");
        for(StorableItem s: items)
            exit = exit.concat(s.toString()+ " ");
        exit = exit.concat("]");
        return exit;
    }
}
