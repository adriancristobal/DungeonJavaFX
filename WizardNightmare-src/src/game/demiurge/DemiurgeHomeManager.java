package game.demiurge;

import game.character.Wizard;
import game.objectContainer.exceptions.ContainerEmptyException;
import game.objectContainer.exceptions.ContainerErrorException;
import game.dungeon.HomeNotEnoughSingaException;
import game.spell.SpellUnknowableException;
import game.object.SingaCrystal;
import game.objectContainer.Container;
import game.spell.Spell;
import game.spellContainer.Knowledge;
import game.dungeon.Home;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardSpellKnownException;
import game.character.exceptions.WizardTiredException;
import game.util.ValueOverMaxException;

public class DemiurgeHomeManager {
    private DungeonConfiguration dc;
    private Wizard wizard;
    private Home home;
    private DemiurgeContainerManager containerManager;

    public DemiurgeHomeManager(DungeonConfiguration dc, Wizard wizard, Home home, DemiurgeContainerManager dcm){
        this.dc = dc;
        this.wizard = wizard;
        this.home = home;
        containerManager = dcm;
    }


    public String homeInfo() { return home.toString();}
    public String wizardInfo() {return wizard.toString();}

    public void checkWeapon() { wizard.checkWeapon(); }

    public int getLife(){return wizard.getLife();}
    public int getLifeMax() {return wizard.getLifeMax();}

    public int getSinga(){ return home.getSinga();}
    public int getSingaSpace() { return home.getSingaSpace(); }

    public int getSingaPerLifePoint() { return dc.getSingaPerLifePointCost(); }

    public Container getCarrier(){ return wizard.getCrystalCarrier(); }

    public void enterHome(){ containerManager.setSite(home.getContainer()); }

    public void recover(int points) throws HomeNotEnoughSingaException, WizardTiredException, ValueOverMaxException {
        if (points * dc.getSingaPerLifePointCost() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else {
            wizard.recoverLife(points);
            home.useSinga(points * dc.getSingaPerLifePointCost());
            wizard.drainEnergy(dc.getBasicEnergyConsumption());
        }
    }

    public void mergeCrystal(int position) throws WizardTiredException, ContainerEmptyException, ContainerErrorException {
        home.mergeCrystal((SingaCrystal) wizard.getCrystalCarrier().remove(position));
        wizard.drainEnergy(dc.getBasicEnergyConsumption());
    }

    public void upgradeLifeMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (wizard.getLifeMax() * dc.getBasicUpgradeCost() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getLifeMax() / dc.getBasicUpgradeCost() > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            wizard.upgradeLifeMax(dc.getBasicIncrease());
            home.useSinga(wizard.getLifeMax() * dc.getBasicUpgradeCost());
            wizard.drainEnergy(wizard.getEnergyMax() / dc.getBasicUpgradeCost());
        }
    }

    public void upgradeEnergyMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (wizard.getEnergyMax() * dc.getBasicUpgradeCost() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergyMax() / dc.getBasicUpgradeCost() > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            wizard.upgradeEnergyMax(dc.getBasicIncrease());
            home.useSinga(wizard.getEnergyMax() * dc.getBasicUpgradeCost());
            wizard.drainEnergy(wizard.getEnergyMax() / dc.getBasicUpgradeCost());
        }
    }

    public void upgradeComfort() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (home.getComfort() * dc.getComfortUpgradeCost() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (home.getComfort() > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            home.upgradeComfort();
            home.useSinga(home.getComfort() * dc.getComfortUpgradeCost());
            wizard.drainEnergy(home.getComfort());
        }
    }

    public void upgradeSingaMax() throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException {
        if (home.getMaxSinga() > home.getSinga()) {
            throw new HomeNotEnoughSingaException();
        } else if (home.getMaxSinga() / 2 > wizard.getEnergy()) {
            throw new WizardNotEnoughEnergyException();
        } else {
            home.upgradeMaxSinga(dc.getStoneIncrease());
            home.emptySinga();
            wizard.drainEnergy(home.getMaxSinga() / 2);
        }
    }

    public Knowledge getMemory() { return wizard.getMemory(); }
    public void improveSpell(int option) throws ValueOverMaxException, WizardTiredException, HomeNotEnoughSingaException, WizardNotEnoughEnergyException {
        Spell spell = wizard.getSpell(option);
        int nextLevel = spell.getLevel()+dc.getBasicIncrease();

        if (home.getSinga() < nextLevel) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergy() < nextLevel) {
            throw new WizardNotEnoughEnergyException();
        } else {
            spell.improve(dc.getBasicIncrease());
            wizard.drainEnergy(nextLevel);
            home.useSinga(nextLevel);
        }
    }

    public Knowledge getLibrary() { return home.getLibrary(); }
    public void learnSpell(int option) throws HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardTiredException, WizardSpellKnownException {
        Spell spell = home.getSpell(option);

        if (home.getSinga() < 1) {
            throw new HomeNotEnoughSingaException();
        } else if (wizard.getEnergy() < 1) {
            throw new WizardNotEnoughEnergyException();
        } else if (wizard.existsSpell(spell)) {
            throw new WizardSpellKnownException();
        } else {
            try {
                wizard.addSpell(spell);
                wizard.drainEnergy(dc.getBasicEnergyConsumption());
                home.useSinga(dc.getBasicEnergyConsumption());
            } catch (SpellUnknowableException ignored) {
            }
        }
    }

}
