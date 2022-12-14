package game.demiurge;

import game.character.Wizard;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardSpellKnowedException;
import game.character.exceptions.WizardTiredException;
import game.dungeon.object.container.Container;
import game.dungeon.object.exceptions.ContainerEmptyException;
import game.dungeon.object.exceptions.ContainerErrorException;
import game.dungeon.object.item.SingaCrystal;
import game.dungeon.spell.Knowledge;
import game.dungeon.spell.Spell;
import game.dungeon.spell.SpellUnknowableException;
import game.dungeon.structure.Home;
import game.dungeon.structure.exceptions.HomeNotEnoughSingaException;
import game.util.ValueOverMaxException;

import static wizardNightmare.DungeonConfigValues.*;

public class DemiurgeHomeManager {
    Home home;
    Wizard wizard;

    public DemiurgeHomeManager(Wizard wizard, Home home){
        this.wizard = wizard;
        this.home = home;
    }

    public int getLife(){return wizard.getLife();}
    public int getLifeMax() {return wizard.getLifeMax();}
    public boolean fullOfLife(){ return wizard.getLife()==wizard.getLifeMax();}


    public int getSinga(){ return home.getSinga();}
    public int getSingaSpace() { return home.getSingaSpace(); }
    public Container getCarrier(){ return wizard.getCrystalCarrier(); }

    public void recover(int points) throws HomeNotEnoughSingaException, WizardTiredException, ValueOverMaxException {
        if (points * SINGA_PER_LIFEPOINT_COST > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else {
            wizard.recoverLife(points);
            home.useSinga(points * SINGA_PER_LIFEPOINT_COST);
            wizard.drainEnergy(1);
        }
    }

    public void upgradeLifeMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (wizard.getLifeMax() * GENERIC_UPGRADE_COST > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getLifeMax() / GENERIC_UPGRADE_COST > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            int amount = wizard.getEnergyMax() / GENERIC_UPGRADE_COST;
            home.useSinga(wizard.getLifeMax() * GENERIC_UPGRADE_COST);
            wizard.upgradeLifeMax(BASIC_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    public void upgradeEnergyMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (wizard.getEnergyMax() * GENERIC_UPGRADE_COST > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergyMax() / GENERIC_UPGRADE_COST > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            int amount = wizard.getEnergyMax() / GENERIC_UPGRADE_COST;
            home.useSinga(wizard.getEnergyMax() * GENERIC_UPGRADE_COST);
            wizard.upgradeEnergyMax(BASIC_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    public void mergeCrystal(int position) throws WizardTiredException, ContainerEmptyException, ContainerErrorException {
        home.mergeCrystal((SingaCrystal) wizard.getCrystalCarrier().remove(position));
        wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
    }

    public void upgradeComfort() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (home.getComfort() * COMFORT_UPGRADE_COST > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (home.getComfort() > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            int amount = home.getComfort();
            home.useSinga(home.getComfort() * COMFORT_UPGRADE_COST);
            home.upgradeComfort();
            wizard.drainEnergy(amount);
        }
    }

    public void upgradeSingaMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (home.getMaxSinga() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (home.getMaxSinga() / 2 > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            int amount = home.getMaxSinga() / 2;
            home.emptySinga();
            home.upgradeMaxSinga(STONE_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    public Knowledge getMemory() { return wizard.getMemory(); }
    public void improveSpell(int option) throws ValueOverMaxException, WizardTiredException, HomeNotEnoughSingaException, WizardNotEnoughEnergyException {
        Spell spell = wizard.getSpell(option);
        int nextLevel = spell.getLevel()+1;

        if (home.getSinga() > nextLevel) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergy() > nextLevel) {
            throw new WizardNotEnoughEnergyException();
        } else {
            spell.improve();
            wizard.drainEnergy(nextLevel);
            home.useSinga(nextLevel);
        }
    }

    public Knowledge getLibrary() { return home.getLibrary(); }
    public void learnSpell(int option) throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException, WizardSpellKnowedException {
        Spell spell = home.getSpell(option);

        if (home.getSinga() > 1) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergy() > 2) {
            throw new WizardNotEnoughEnergyException();
        } else if (wizard.existsSpell(spell)){
            throw new WizardSpellKnowedException();
        } else {
            try {
                wizard.addSpell(spell);
                wizard.drainEnergy(1);
                home.useSinga(1);
            } catch (SpellUnknowableException ignored) {
            }
        }
    }

}
