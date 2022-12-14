package game.dungeon.structure;

import game.dungeon.object.container.Chest;
import game.dungeon.object.item.SingaCrystal;
import game.dungeon.object.item.SingaStone;
import game.dungeon.spell.Knowledge;
import game.dungeon.spell.Spell;
import game.dungeon.structure.exceptions.HomeNotEnoughSingaException;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;

public class Home extends Site {
    private int comfort;
    private final SingaStone singa;

    private Knowledge library;

    public Home(String desc, int c, int s, int m, Chest chest, Knowledge l) {
        super(-1, desc, chest);
        comfort = c;
        singa = new SingaStone(s, m);
        library = l;
    }

    //Comfort
    public int getComfort() {
        return comfort;
    }
    public void upgradeComfort() {
        comfort++;
    }

    //Singa
    public int getSinga() {
        return singa.getValue();
    }
    public int getMaxSinga() { return singa.getMaximum(); }
    public int getSingaSpace() {
        return singa.availableToMaximum();
    }
    public void emptySinga() {
        singa.setToMinimum();
    }
    public void upgradeMaxSinga(int s) {
        singa.increaseMaximum(s);
    }
    public void mergeCrystal(SingaCrystal c) {
        try {
            singa.increaseValue(c.getValue());
        } catch (ValueOverMaxException e) {
            singa.setToMaximum();
        }
    }
    public void useSinga(int s) throws HomeNotEnoughSingaException {
        try {
            singa.decreaseValue(s);
        } catch (ValueUnderMinException e) {
            throw new HomeNotEnoughSingaException();
        }
    }


    public Knowledge getLibrary() { return library; }
    public Spell getSpell(int index){ return library.get(index); }

    public String toString() {
        String exit = "HOME";
        exit = exit.concat("\n\tComfort(" + comfort + ") Singa" + singa.toString());
        exit = exit.concat("\n\tChest" + container.toString());
        return exit;
    }



}
