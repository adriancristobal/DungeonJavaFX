package nightmare.management;

import nightmare.dungeon.*;
import nightmare.items.*;
import nightmare.pj.*;
import nightmare.pnj.Creature;
import nightmare.pnj.CreatureKilledException;

import java.util.ArrayList;

import static nightmare.management.ConfigValues.*;

public class Demiurge {
    private DungeonLoader dungeonLoader;
    private int day;
    private Dungeon dungeon;
    private Room currentRoom;
    private Home home;
    private Wizard wizard;


    public Demiurge(DungeonLoader dl) {
        dungeonLoader = dl;
        day = 0;
    }

    void createEnvironment() {
        dungeonLoader.load(this);
        nextDay();
    }

    int getDay() {
        return day;
    }

    void nextDay() {
        wizard.sleep(home.getComfort() * COMFORT_MODIFIER_FOR_ENERGY);
        dungeon.generateCrystals(CRYSTALS_PER_DAY, SINGA_PER_CRYSTAL);
        currentRoom = null;
        day++;
    }

    void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    Room getCurrentSite() {
        return currentRoom;
    }

    Home getHome() {
        return home;
    }

    void setHome(Home home) {
        this.home = home;
    }

    Wizard getWizard() {
        return wizard;
    }

    void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    void setWizardName(String n) {
        wizard.setName(n);
    }

    ArrayList<Crystal> getWizardCrystals() {
        return wizard.getCrystalCarrier().getCrystals();
    }

    void recover(int points) throws HomeNoSingaException, WizardLifePointsExcededException, WizardTiredException {
        if (wizard.getLife() + points > wizard.getLifeMax()) {
            throw new WizardLifePointsExcededException();
        } else if (points * SINGA_PER_LIFEPOINT_COST > home.getSinga()) {
            throw new HomeNoSingaException();
        } else {
            wizard.recoverLife(points);
            home.useSinga(points * SINGA_PER_LIFEPOINT_COST);
            wizard.drainEnergy(1);
        }
    }

    void upgradeLifeMax() throws HomeNoSingaException, WizardNoEnergyException, WizardTiredException {
        if (wizard.getLifeMax() * GENERIC_UPGRADE_COST > home.getSinga()) {
            throw new HomeNoSingaException();
        } else if (wizard.getLifeMax() / GENERIC_UPGRADE_COST > wizard.getEnergy()) {
            throw new WizardNoEnergyException();
        } else {
            int amount = wizard.getEnergyMax() / GENERIC_UPGRADE_COST;
            home.useSinga(wizard.getLifeMax() * GENERIC_UPGRADE_COST);
            wizard.upgradeLifeMax(BASIC_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    void upgradeEnergyMax() throws HomeNoSingaException, WizardNoEnergyException, WizardTiredException {
        if (wizard.getEnergyMax() * ConfigValues.GENERIC_UPGRADE_COST > home.getSinga()) {
            throw new HomeNoSingaException();
        } else if (wizard.getEnergyMax() / ConfigValues.GENERIC_UPGRADE_COST > wizard.getEnergy()) {
            throw new WizardNoEnergyException();
        } else {
            int amount = wizard.getEnergyMax() / ConfigValues.GENERIC_UPGRADE_COST;
            home.useSinga(wizard.getEnergyMax() * ConfigValues.GENERIC_UPGRADE_COST);
            wizard.upgradeEnergyMax(BASIC_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    void upgradeComfort() throws HomeNoSingaException, WizardNoEnergyException, WizardTiredException {
        if (home.getComfort() * COMFORT_UPGRADE_COST > home.getSinga()) {
            throw new HomeNoSingaException();
        } else if (home.getComfort() > wizard.getEnergy()) {
            throw new WizardNoEnergyException();
        } else {
            int amount = home.getComfort();
            home.useSinga(home.getComfort() * COMFORT_UPGRADE_COST);
            home.upgradeComfort();
            wizard.drainEnergy(amount);
        }
    }

    void upgradeSingaMax() throws HomeNoSingaException, WizardNoEnergyException, WizardTiredException {
        if (home.getMaxSinga() > home.getSinga()) {
            throw new HomeNoSingaException();
        } else if (home.getMaxSinga() / 2 > wizard.getEnergy()) {
            throw new WizardNoEnergyException();
        } else {
            int amount = home.getMaxSinga() / 2;
            home.emptySinga();
            home.upgradeMaxSinga(STONE_INCREASE);
            wizard.drainEnergy(amount);
        }
    }

    void mergeCrystal(int position) throws WizardTiredException {
        home.mergeCrystal(wizard.getCrystalCarrier().getCrystals().remove(position));
        wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
    }

    void gatherCrystals() throws CrystalCarrierFullException, RoomCrystalEmptyException {
        if (wizard.getCrystalCarrier().isFull())
            throw new CrystalCarrierFullException();
        else if (currentRoom.numberOfCrystals() == 0)
            throw new RoomCrystalEmptyException();
        else
            while (currentRoom.numberOfCrystals() != 0)
                wizard.addCrystal(currentRoom.gather());
    }

    void storageAddItem(int i) throws StorageFullException {
        StorableItem si = wizard.getWereables().get(i);
        home.getStorage().add(si);
        wizard.removeItem(si);
    }

    void storageDeleteItem(int i) {
        home.getStorage().remove(i);
    }

    void storagePutItem(int i) throws WereablesFullException {
        StorableItem si = home.getStorage().get(i);
        wizard.addItem(si);
        home.getStorage().remove(i);
    }

    void storageExchangeItem(int fromStorage, int fromWizard) throws WereableIncompatibleException {
        StorableItem s = home.getStorage().get(fromStorage);
        StorableItem w = wizard.getWereables().get(fromWizard);
        wizard.exchangeItem(w, s);
        home.getStorage().remove(fromStorage);
        home.getStorage().add(w);
    }

    void enterDungeon() throws WizardTiredException {
        try {
            openDoor(0);
        } catch (HomeBackException ignored) {
        }
    }

    void openDoor(int index) throws WizardTiredException, HomeBackException {
        Site site;
        if (currentRoom == null)
            site = home.doorAt(0).openFrom(home);
        else
            site = currentRoom.getDoors().get(index).openFrom(currentRoom);
        wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
        if (site == home) {
            currentRoom = null;
            throw new HomeBackException();
        } else
            currentRoom = (Room) site;
    }

    void roomTakeItem(int i) throws WereablesFullException {
        StorableItem si = currentRoom.getItems().get(i);
        wizard.addItem(si);
        currentRoom.removeItem(si);
    }

    void roomExchangeItem(int fromRoom, int fromWizard) throws WereableIncompatibleException {
        StorableItem s = currentRoom.getItems().get(fromRoom);
        StorableItem w = wizard.getWereables().get(fromWizard);
        wizard.exchangeItem(w, s);
        currentRoom.removeItem(s);
        currentRoom.addItem(w);
    }

    boolean canRunAway() {
        return (int) (Math.random() * 100 + 1) > MINIMUM_FOR_RUN_AWAY;
    }

    boolean priority() {
        return wizard.getEnergy() >= currentRoom.getCreature().getLife();
    }

    boolean wizardAttack() throws CreatureKilledException, WizardNoWeaponException, WizardTiredException {
        boolean success = false;
        int value = FIGHT_SUCCESS_LOW;
        Creature c = currentRoom.getCreature();
        Weapon w = wizard.getWeapon();

        if (wizard.getEnergy() > wizard.getEnergyMax()/10*8)
            value = FIGHT_SUCCESS_HIGHT;
        else if (wizard.getEnergy() > wizard.getEnergyMax()/10*4)
            value = FIGHT_SUCCESS_MEDIUM;

        if((int)(Math.random()*100+1) > value) {
            success = true;
            c.beat(w.getValue());
            wizard.drainEnergy(BASIC_ENERGY_CONSUMPTION);
        }
        return success;
    }

    boolean creatureAttack() throws WizardDeathException {
        boolean success = false;
        int value = FIGHT_SUCCESS_LOW;
        Creature c = currentRoom.getCreature();

        if (c.getLife() > wizard.getEnergy())
            value = FIGHT_SUCCESS_HIGHT;
        else if (c.getLife() == wizard.getEnergy())
            value = FIGHT_SUCCESS_MEDIUM;

        if((int)(Math.random()*100+1) > value) {
            wizard.drainLife(c.getPunch());
            success = true;
        }
        return success;
    }

}
