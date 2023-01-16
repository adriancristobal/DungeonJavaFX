package game.spellContainer;

import game.spell.Spell;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Basic aggregation of elements that is limited in capacity
 */
public abstract class Knowledge {

    final ArrayList<Spell> spells;

    public Knowledge() { spells = new ArrayList<>(); }

    public int size() { return spells.size(); }

    public Spell get(int index){ return spells.get(index); }

    public void add(Spell i) { spells.add(i); }

    public boolean exists ( Spell spell) {
        for (Spell s: spells)
            if (s.getClass().getName().equals(spell.getClass().getName()))
                return true;
        return false;
    }

    public Iterator iterator(){ return spells.iterator(); }

    public String toString() {
        String exit = getClass().getSimpleName() + " -> ";
        for (Spell spell : spells)
            exit = exit.concat(spell + " ");
        return exit;
    }

}
