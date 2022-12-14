package nightmare.dungeon;

import nightmare.items.Crystal;
import nightmare.items.StorableItem;
import nightmare.util.AttributeControl;
import nightmare.util.AttributeException;

import java.util.ArrayList;

public class Home extends Site {
    private int comfort;
    private final AttributeControl singa;
    int capacity;
    private final ArrayList<StorableItem> items;

    public Home(int c, int s, int m, int st) {
        super();
        comfort = c;
        singa = new AttributeControl(s, m);
        capacity = st;
        items = new ArrayList<>(c);
    }

    //Comfort Methods
    public int getComfort(){
        return comfort;
    }

    public void upgradeComfort(){
        comfort++;
    }

    //SINGA Methods
    public int getSinga(){
        return singa.getCurrentValue();
    }

    public void mergeCrystal(Crystal c){
        try {
            singa.increaseCurrentValue(c.getSinga());
        } catch (AttributeException e) {
            singa.setToMax();
        }
    }

    public void useSinga(int s) throws HomeNoSingaException {
        try {
            singa.decreaseCurrentValue(s);
        } catch (AttributeException e) {
            throw new HomeNoSingaException();
        }
    }

    public int getMaxSinga() {
        return singa.getMaxValue();
    }

    public void emptySinga() {
        singa.reset();
    }

    public void upgradeMaxSinga(int s) { singa.upgradeMaxValue(s); }

    public int getSpaceForSinga() {
        return singa.getDifference();
    }

    //STORAGE Methods
    public ArrayList<StorableItem> getStorage(){ return items; }

    public boolean isStorageFull() {return items.size() == capacity; }


    public String toString(){
        String exit = "HOME";
        exit = exit.concat("\n\tComfort("+comfort+") Singa"+singa.toString());
        exit = exit.concat("\n\tItems: Capacity(" + capacity + ") [");
        for(StorableItem s: items)
            exit = exit.concat(s.toString()+ " ");
        exit = exit.concat("]");
        return exit;
    }

}
