package game.character;

import game.Domain;
import game.actions.Attack;
import game.character.exceptions.WizardTiredException;
import game.object.Item;
import game.object.Weapon;
import game.objectContainer.Container;
import game.objectContainer.CrystalCarrier;
import game.objectContainer.JewelryBag;
import game.objectContainer.Wearables;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.Spell;
import game.spell.SpellUnknowableException;
import game.util.Value;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;


/**
 * Wizard's attributes and related data.
 *
 */
public class Wizard extends Character {

    private final Value energy;
    private final Wearables wearables;
    private final CrystalCarrier crystalCarrier;
    private final JewelryBag jewelryBag;

    public Wizard(String n, int l, int lm, int e, int em, Wearables w, CrystalCarrier c, JewelryBag j) {
        super(n, Domain.NONE, l, lm, 1);

        energy = new Value(em, 0, e);

        wearables = w;
        crystalCarrier = c;
        jewelryBag = j;
    }

    //Energy
    public int getEnergy() {
        return energy.getValue();
    }
    public boolean hasEnoughEnergy(int checkValue) {return energy.availableToMinimum() > checkValue; }

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
    public Container getWearables() { return wearables; }


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

    public void checkWeapon(){
        for (Attack a : attacks)
            if (a instanceof Weapon)
                attacks.remove(a);

        Weapon w = wearables.getWeapon();
        if(w != null)
            attacks.add(w);
    }

    public int protect(int damage, Domain domain){
        int newDamage = damage - wearables.getRingProtection(domain);;
        if(newDamage < 1)
            newDamage = 1;
        return  newDamage;
    }

    public String toString() {
        return name + "\tEnergy" + energy + "\tLife" + life + "\n\t"
                + crystalCarrier + "\n\t" + wearables + "\n\t" + jewelryBag + "\n\t" + memory;
    }

}
