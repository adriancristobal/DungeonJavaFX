package game.demiurge;

import game.character.Wizard;
import game.dungeon.structure.*;


import static wizardNightmare.DungeonConfigValues.*;

public class Demiurge {
    private int day;
    private Dungeon dungeon;
    private Home home;
    private Wizard wizard;

    DemiurgeHomeManager homeManager;
    DemiurgeContainerManager containerManager;
    DemiurgeDungeonManager dungeonManager;

    public Demiurge() { day = 0;  }

    public DemiurgeHomeManager getHomeManager() { return homeManager; }
    public DemiurgeContainerManager getContainerManager() { return containerManager; }
    public DemiurgeDungeonManager getDungeonManager() { return dungeonManager; }

    public void loadEnvironment(DungeonLoader dungeonLoader) {
        dungeonLoader.load(this);
        containerManager = new DemiurgeContainerManager(wizard.getWereables(), wizard.getJewelryBag(), home.getContainer());
        homeManager =  new DemiurgeHomeManager(wizard, home);
        dungeonManager = new DemiurgeDungeonManager(wizard, home, containerManager);
        nextDay();
    }

    public int getDay() {
        return day;
    }
    public void nextDay() {
        wizard.sleep(home.getComfort() * COMFORT_MODIFIER_FOR_ENERGY);
        dungeon.generateCrystals(CRYSTALS_PER_DAY, SINGA_PER_CRYSTAL);
        day++;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }
    public void setHome(Home home) {
        this.home = home;
    }
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public String homeInfo() { return home.toString();}
    public String wizardInfo() {return wizard.toString();}

}
