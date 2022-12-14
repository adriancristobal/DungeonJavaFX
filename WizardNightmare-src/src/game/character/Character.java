package game.character;

import game.character.exceptions.WizardDeathException;
import game.dungeon.Attack;
import game.dungeon.spell.Knowledge;
import game.dungeon.spell.Spell;
import game.dungeon.spell.SpellUnknowableException;
import game.util.Value;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Wizard's attributes and related data.
 *
 */
public abstract class Character {

    String name;
    final Value life;
    Knowledge memory;
    ArrayList<Attack> attacks;

    /**
     * @param n name
     * @param l Initial life
     * @param lm Maximun life
     */
    public Character(String n, int l, int lm) {
        name = n;
        life = new Value(l, 0, lm);
        memory = new Knowledge();
        attacks = new ArrayList<>();
    }

    //Life
    public int getLife() { return life.getValue(); }
    public int getLifeMax() {
        return life.getMaximum();
    }
    public void upgradeLifeMax(int m) {
        life.increaseMaximum(m);
    }
    public void recoverLife(int v) throws ValueOverMaxException { life.increaseValue(v); }
    public void drainLife(int v) throws WizardDeathException {
        try {
            life.decreaseValue(v);
        } catch (ValueUnderMinException e) {
            life.setToMinimum();
            throw new WizardDeathException();
        }
    }


    //Spells
    public Knowledge getMemory() { return memory; }
    public void addSpell(Spell spell) throws SpellUnknowableException {
        if(spell instanceof Attack)
            attacks.add((Attack) spell);
        memory.add(spell);
    }
    public Spell getSpell(int index){ return memory.get(index); }
    public boolean existsSpell(Spell spell) { return memory.exists(spell); }


    //Attacks
    public Iterator getAttacks() { return attacks.iterator(); }
    public int getAttacksNumber() { return attacks.size(); }
    public Attack getAttack(int index) { return attacks.get(index); }

}
