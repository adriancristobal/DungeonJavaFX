package nightmare.pj;

import nightmare.items.*;
import nightmare.spells.Spell;
import nightmare.util.AttributeControl;
import nightmare.util.AttributeException;

import java.util.ArrayList;

public class Wizard {
    private String name;

    private final AttributeControl life;
    private final AttributeControl energy;
    private CrystalCarrier crystalCarrier;
    private final Wereables wereables;
    private final ArrayList<Spell> spells;


    public Wizard(String n, int l, int lm, int e, int em, CrystalCarrier c, Wereables w){
        name = n;

        life = new AttributeControl(l, lm);
        energy = new AttributeControl(e, em);

        crystalCarrier = c;
        wereables = w;
        spells = new ArrayList<>();
    }

    public String getName(){return name;}

    public void setName(String n) {
        name = n;
    }

    //ABOUT LIFE
    public int getLife(){ return life.getCurrentValue(); }

    public void recoverLife(int v) throws WizardLifePointsExcededException {
        try {
            life.increaseCurrentValue(v);
        } catch (AttributeException e) {
            throw new WizardLifePointsExcededException();
        }
    }

    public void drainLife(int v) throws WizardDeathException {
        try {
            life.decreaseCurrentValue(v);
        } catch (AttributeException e) {
            throw new WizardDeathException();
        }
    }

    public int getLifeMax(){ return life.getMaxValue(); }

    public void upgradeLifeMax(int m){
        life.upgradeMaxValue(m);
    }

    //ABOUT ENERGY
    public int getEnergy(){ return energy.getCurrentValue(); }

    public void sleep(int maxRecovery){
        recoverEnergy(maxRecovery);
    }

    public void recoverEnergy(int e){
        try {
            energy.increaseCurrentValue(e);
        } catch (AttributeException ex) {
            energy.setToMax();
        }
    }

    public void drainEnergy(int e) throws WizardTiredException {
        try {
            life.decreaseCurrentValue(e);
        } catch (AttributeException ex) {
            throw new WizardTiredException();
        }
        if (energy.getCurrentValue() <= 1)
            throw new WizardTiredException();
    }

    public int getEnergyMax(){ return energy.getMaxValue(); }

    public void upgradeEnergyMax(int m){
        energy.upgradeMaxValue(m);
    }

    //ABOUT CRYSTAL CARRIER
    public CrystalCarrier getCrystalCarrier(){ return crystalCarrier; }

    public void addCrystal(Crystal crystal) throws CrystalCarrierFullException {
        if (crystalCarrier.isFull())
            throw new CrystalCarrierFullException();
        else
            crystalCarrier.add(crystal);
    }

    public CrystalCarrier changeCrystalCarrier(CrystalCarrier n){
        CrystalCarrier v = crystalCarrier;
        n.addAll(crystalCarrier.removeAll());
        crystalCarrier = n;
        return v;
    }

    //ABOUT WEREABLES
    public ArrayList<StorableItem> getWereables(){
        return wereables.getItems();
    }

    public void addItem(StorableItem si) throws WereablesFullException {
        wereables.addWereable(si);
    }

    public void removeItem(StorableItem si) {
        wereables.removeWereable(si);
    }

    public void exchangeItem(StorableItem o, StorableItem n) throws WereableIncompatibleException {
        wereables.exchangeWereable(o, n);
    }

    public Weapon getWeapon () throws WizardNoWeaponException {
        return wereables.getWeapon();
    }

    public String toString(){
        String exit = name;
        exit = exit.concat("\n\tEnergy"+energy);
        exit = exit.concat(" Life"+life);
        exit = exit.concat(" Crystals"+crystalCarrier);
        exit = exit.concat("\n\tItems"+wereables);
        exit = exit.concat("\n\tSpells"+spells);
        return exit;
    }
}
