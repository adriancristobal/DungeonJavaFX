package game.dungeon;

import game.object.SingaCrystal;
import game.object.SingaStone;
import game.objectContainer.Chest;
import game.spell.Spell;
import game.spellContainer.Knowledge;
import game.util.Value;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;

public class Home extends Site {
    private Value comfort;
    private final SingaStone singa;

    private Knowledge library;

    public Home(String desc, int c, int s, int m, Chest chest, Knowledge l) {
        super(-1, desc, chest);
        comfort = new Value(c);
        singa = new SingaStone(s, m);
        library = l;
    }

    //Comfort
    public int getComfort() {
        return comfort.getValue();
    }
    public void upgradeComfort() { comfort.increaseMaximum(1); }

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


    public String toString() { return "HOME " + comfort + "\n\t" + singa + "\n\t" + container + "\n\t" + library; }



}
