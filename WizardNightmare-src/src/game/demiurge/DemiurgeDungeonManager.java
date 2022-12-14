package game.demiurge;


import game.character.Creature;
import game.character.exceptions.CreatureKilledException;
import game.character.Wizard;
import game.character.exceptions.WizardDeathException;
import game.character.exceptions.WizardNoWeaponException;
import game.character.exceptions.WizardTiredException;
import game.dungeon.Attack;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;
import game.dungeon.object.item.Weapon;
import game.dungeon.spell.Knowledge;
import game.dungeon.structure.*;
import game.dungeon.structure.exceptions.HomeBackException;
import game.dungeon.structure.exceptions.RoomCrystalEmptyException;

import java.util.Iterator;

import static wizardNightmare.DungeonConfigValues.*;

public class DemiurgeDungeonManager {

    private Site site;
    private final Wizard wizard;
    private Creature creature;
    DemiurgeContainerManager containerManager;

    public DemiurgeDungeonManager(Wizard wizard, Site site, DemiurgeContainerManager dcm){
        this.wizard = wizard;
        this.site = site;
        containerManager = dcm;
    }

    public String getRoomInfo() { return site.toString(); }
    public int getNumberOfDoors() { return site.getNumberOfDoors(); }
    public Iterator getDoorsIterator() { return site.iterator(); }

    public boolean hasCreature() { return creature != null; }
    public boolean isAlive() { return ((Room)site).isAlive(); }

    public void enterDungeon() throws WizardTiredException {
        try {
            openDoor(0);
        } catch (HomeBackException ignored) {
        }
    }

    public void openDoor(int index) throws WizardTiredException, HomeBackException {

        site = site.openDoor(index);

        wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
        containerManager.setSite(site.getContainer());
        if (site instanceof Home) {
            throw new HomeBackException();
        } else {
            Room currentRoom = (Room)site;
            creature = currentRoom.getCreature();
        }
    }

    public void gatherCrystals() throws RoomCrystalEmptyException, ContainerFullException {
        Room currentRoom = (Room)site;
        while (currentRoom.numberOfCrystals() != 0) {
            if (currentRoom.numberOfCrystals() == 0)
                throw new RoomCrystalEmptyException();
            else
                try {
                    wizard.getCrystalCarrier().add(currentRoom.gather());
                } catch (ContainerUnacceptedItemException ignored) {
                }
        }
    }

    //CREATURE methods
    public boolean canRunAway() { return (int) (Math.random() * 100 + 1) > MINIMUM_FOR_RUN_AWAY; }
    public boolean priority() { return wizard.getEnergy() >= creature.getLife(); }

    public Iterator getAttacks() { return wizard.getAttacks(); }
    public int getAttacksNumber() { return wizard.getAttacksNumber(); }
    public Attack getAttack(int index) { return wizard.getAttack(index); }

    public Knowledge getMemory() { return wizard.getMemory(); }

    public boolean wizardAttack(Attack attack) throws CreatureKilledException, WizardNoWeaponException, WizardTiredException {
        boolean success = false;
        int value = FIGHT_SUCCESS_LOW;

        if (wizard.getEnergy() > wizard.getEnergyMax() / 10 * 8)
            value = FIGHT_SUCCESS_HIGHT;
        else if (wizard.getEnergy() > wizard.getEnergyMax() / 10 * 4)
            value = FIGHT_SUCCESS_MEDIUM;

        if ((int) (Math.random() * 100 + 1) > value) {
            success = true;
            creature.beat(attack.getAttackValue());
            wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
        }
        return success;
    }

    public boolean creatureAttack() throws WizardDeathException {
        boolean success = false;
        int value = FIGHT_SUCCESS_LOW;

        if (creature.getLife() > wizard.getEnergy())
            value = FIGHT_SUCCESS_HIGHT;
        else if (creature.getLife() == wizard.getEnergy())
            value = FIGHT_SUCCESS_MEDIUM;

        if ((int) (Math.random() * 100 + 1) > value) {
            wizard.drainLife(creature.randomAttack());
            success = true;
        }
        return success;
    }

}
