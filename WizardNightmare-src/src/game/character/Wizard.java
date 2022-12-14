package game.character;

import game.character.exceptions.WizardTiredException;
import game.dungeon.Attack;
import game.dungeon.object.container.*;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Item;
import game.dungeon.spell.Spell;
import game.dungeon.spell.SpellUnknowableException;
import game.util.Value;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;

import java.util.ArrayList;


/**
 * Wizard's attributes and related data.
 *
 */
public class Wizard extends Character {

    private final Value energy;
    private final CrystalCarrier crystalCarrier;
    private final JewelryBag jewelryBag;
    private final Wearables wearables;

    public Wizard(String n, int l, int lm, int e, int em, CrystalCarrier c, Wearables w, JewelryBag j) {
        super(n, l, lm);

        energy = new Value(em, 0, e);

        crystalCarrier = c;
        wearables = w;
        jewelryBag = j;
    }

    //Energy
    public int getEnergy() {
        return energy.getValue();
    }

    public void sleep(int maxRecovery) {
        recoverEnergy(maxRecovery);
    }
    public void recoverEnergy(int e) {
        try {
            energy.increaseValue(e);
        } catch (ValueOverMaxException ex) {
            energy.setToMaximum();
        }
    }
    public void drainEnergy(int e) throws WizardTiredException {
        try {
            energy.decreaseValue(e);
        } catch (ValueUnderMinException ex) {
            energy.setToMinimum();
        }
        if (energy.getValue() <= 1)
            throw new WizardTiredException();
    }
    public int getEnergyMax() {
        return energy.getMaximum();
    }
    public void upgradeEnergyMax(int m) {
        energy.increaseMaximum(m);
    }


    //Containers
    public Container getCrystalCarrier(){ return crystalCarrier;}
    public Container getJewelryBag() { return jewelryBag; }
    public Container getWereables() { return wearables; }


    public void addItem(Item item) throws ContainerUnacceptedItemException, ContainerFullException {
        if(item instanceof Attack)
            attacks.add((Attack) item);
        wearables.add(item);
    }


    public void addSpell(Spell spell) throws SpellUnknowableException {
        if(spell instanceof Attack)
            attacks.add((Attack) spell);
        memory.add(spell);
    }





    public String toString() {
        String exit = super.name;
        exit = exit.concat("\n\tEnergy" + energy);
        exit = exit.concat(" Life" + life);
        exit = exit.concat(" Crystals" + crystalCarrier);
        exit = exit.concat("\n\tItems" + wearables);
        exit = exit.concat("\n\tSpells" + memory);
        return exit;
    }



}
