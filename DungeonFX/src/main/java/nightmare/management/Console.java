package nightmare.management;

import nightmare.items.*;
import nightmare.pj.*;
import nightmare.dungeon.*;
import nightmare.pnj.CreatureKilledException;

import java.util.InputMismatchException;
import java.util.Scanner;

import static nightmare.management.ConfigValues.*;

public class Console {
    Scanner sn;
    Demiurge demiurge;

    public Console(Demiurge d){
        demiurge = d;
        sn = new Scanner(System.in);
    }

    public void start(){


        System.out.println("------------");
        System.out.println("CONSOLE MODE");
        System.out.println("------------");
        System.out.println();
        System.out.println("Waking up Demiurge...");
        System.out.println();
        System.out.println("Preparing the dormant body suspension environment...");
        demiurge.createEnvironment();
/*
        String name;
        String s;
        System.out.println("Set wizard's name: ");
        demiurge.setWizardName(sn.next());
*/
        System.out.println();
        System.out.println("Waking up " + demiurge.getWizard().getName());
        menuHome();
    }

    void menuHome(){
        int option;

        while (true) {
            System.out.println();
            System.out.println("-------");
            System.out.println("-HOME-");
            System.out.println("-------");
            System.out.println(demiurge.getHome().toString());
            System.out.println(demiurge.getWizard().toString());
            System.out.println();
            System.out.println("Select: ");
            System.out.println("0.- Exit Game");
            System.out.println("1.- Sleep (recover energy)");
            System.out.println("2.- Recover life points");
            System.out.println("3.- Upgrade max life");
            System.out.println("4.- Upgrade max energy");
            System.out.println("5.- Upgrade home comfort");
            System.out.println("6.- Upgrade stone capacity");
            System.out.println("7.- Merge crystals");
            System.out.println("8.- Manage storage");
            System.out.println("9.- Go to Dungeon");
            System.out.print("So...: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {
                        System.out.println("Bye bye looser!");
                        return;
                    }
                    case 1 -> {
                        demiurge.nextDay();
                        showSleep();
                    }
                    case 2 -> menuHomeRecover();
                    case 3 -> demiurge.upgradeLifeMax();
                    case 4 -> demiurge.upgradeEnergyMax();
                    case 5 -> demiurge.upgradeComfort();
                    case 6 -> demiurge.upgradeSingaMax();
                    case 7 -> menuHomeCrystals();
                    case 8 -> menuHomeStore();
                    case 9 -> menuDungeon();
                    default -> System.out.println("Select a number between 0 and 6");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (HomeNoSingaException e) {
                System.out.println("\nYou don't have enough singa.");
            } catch (WizardNoEnergyException e) {
                System.out.println("\nYou don't have enough energy to do that.");
            } catch (WizardTiredException e) {
                System.out.println("\nYou are very tired and the automatic spell \"Come home\" is cast.");
                demiurge.nextDay();
                showSleep();
            } catch (WizardDeathException e) {
                System.out.println("Your are death!");
                return;
            }
        }
    }

    void menuHomeRecover() throws WizardTiredException {
        int points;

        while (true) {
            System.out.println();
            System.out.println("----------------------");
            System.out.println("Recovering life points");
            System.out.println("----------------------");
            if(demiurge.getWizard().getLife() == demiurge.getWizard().getLifeMax()){
                System.out.println("You are full of life points.");
                break;
            }else{
                System.out.println("You have " + demiurge.getWizard().getLife() + " of " + demiurge.getWizard().getLifeMax() + " points of life.");
                System.out.println("There is " + demiurge.getHome().getSinga() + " singa and recovering each life point costs " + SINGA_PER_LIFEPOINT_COST + " singa.");
                System.out.print("How many do you want to recover (0 to exit)? ");
                try {
                    points = sn.nextInt();
                    if(points == 0)
                        break;
                    else {
                        demiurge.recover(points);
                    }
                } catch (HomeNoSingaException e) {
                    System.out.println("You don't have enough singa.");
                } catch (WizardLifePointsExcededException e) {
                    System.out.println("Too many life points.");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }

            }
        }
    }

    void menuHomeCrystals() throws WizardTiredException {
        int option;
        int shift = 1;

        while (true) {
            System.out.println();
            System.out.println("----------------");
            System.out.println("Merging crystals");
            System.out.println("----------------");
            if(demiurge.getWizardCrystals().size() == 0){
                System.out.println("No crystals to merge.");
                break;
            }else if(demiurge.getHome().getSinga() == demiurge.getHome().getMaxSinga()){
                System.out.println("Your home is full of singa.");
                break;
            }else{
                System.out.println("There is space to store: " + demiurge.getHome().getSpaceForSinga() + " singa.");
                System.out.println("0.- End merge.");
                for(int i = 0 ; i<demiurge.getWizardCrystals().size(); i++)
                    System.out.println((i+shift)+".- To merge crystal with " + demiurge.getWizardCrystals().get(i).getSinga() + " singa.");
                System.out.print("So...: ");
                try {
                    option = sn.nextInt();
                    if(option == 0)
                        break;
                    else if (option > 0 && option <= demiurge.getWizardCrystals().size())
                        demiurge.mergeCrystal(option-shift);
                    else
                        System.out.println("Select a number between 0 and " + demiurge.getWizardCrystals().size() + ".");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }
            }
        }
    }

    void menuHomeStore(){
        int option;

        while (true){
            System.out.println();
            System.out.println("-------");
            System.out.println("Storage");
            System.out.println("-------");
            System.out.println();
            System.out.println("Storage: " + demiurge.getHome().getStorage().toString());
            System.out.println("Wizard items: " + demiurge.getWizard().getWereables().toString());
            System.out.println();
            System.out.println("0.- Exit");
            System.out.println("1.- Add item to storage");
            System.out.println("2.- Delete item from storage");
            System.out.println("3.- Take from storage");
            System.out.println("4.- Exchange items");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {return;}
                    case 1 -> menuHomeStoreAdd();
                    case 2 -> menuHomeStoreDelete();
                    case 3 -> menuHomeStoreTake();
                    case 4 -> menuHomeStoreExchange();
                    default -> System.out.println("Select a number between 0 and 4");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
    }

    void menuHomeStoreAdd(){
        int option;
        int shift = 1;
        int position = shift;
        int wizardItems = demiurge.getWizard().getWereables().size();

        while (true){
            if(demiurge.getWizard().getWereables().size() == 0) {
                System.out.println("Nothing to storage.");
                break;
            }else if(demiurge.getHome().isStorageFull()) {
                System.out.println("There is no space.");
                break;
            }else{
                System.out.println("Select one of your items:");
                System.out.println("\t0.- Exit");
                for(StorableItem s: demiurge.getWizard().getWereables())
                    System.out.println("\t" + position++ + ".- " + s.toString());
                System.out.print("So...: ");
                try {
                    option = sn.nextInt();
                    if(option == 0)
                        break;
                    else if (option > 0 && option <= wizardItems)
                        demiurge.storageAddItem(option-shift);
                    else
                        System.out.println("Select a number between 0 and " + wizardItems);
                } catch (StorageFullException e) {
                    System.out.println("You have to liberate space before adding.");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }
            }
        }
    }

    void menuHomeStoreDelete(){
        int option;
        int shift = 1;
        int position = shift;
        int homeItems = demiurge.getHome().getStorage().size();

        while (true){
            if(demiurge.getHome().getStorage().size() == 0) {
                System.out.println("The storage is empty.");
                return;
            }else{
                System.out.println("Select an item from the storage: ");
                System.out.println("\t0.- Exit");
                for(StorableItem s: demiurge.getHome().getStorage())
                    System.out.println("\t" + position++ + ".- " + s.toString());
                System.out.print("So...: ");
                try {
                    option = sn.nextInt();
                    if(option == 0)
                        break;
                    else if (option > 0 && option <= homeItems) {
                        demiurge.storageDeleteItem(option - shift);
                        break;
                    }else
                        System.out.println("Select a number between 0 and " + homeItems);
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }
            }
        }
    }

    void menuHomeStoreTake() {
        int option;
        int shift = 1;
        int position = shift;
        int homeItems = demiurge.getHome().getStorage().size();

        while (true){
            if(demiurge.getHome().getStorage().size() == 0) {
                System.out.println("The storage is empty.");
                return;
            }else{
                System.out.println("Select an item from the storage: ");
                System.out.println("\t0.- Exit");
                for(StorableItem s: demiurge.getHome().getStorage())
                    System.out.println("\t" + position++ + ".- " + s.toString());
                System.out.print("So...: ");
                try {
                    option = sn.nextInt();
                    if(option == 0)
                        break;
                    else if (option > 0 && option <= homeItems) {
                        demiurge.storagePutItem(option - shift);
                        break;
                    }else
                        System.out.println("Select a number between 0 and " + homeItems);
                } catch (WereablesFullException e) {
                    System.out.println("You can't put this item");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }
            }
        }
    }

    void menuHomeStoreExchange() {
        int fromStorage;
        int fromWizard;
        int shift = 1;
        int position = shift;
        int homeItems = demiurge.getHome().getStorage().size();
        int wizardItems = demiurge.getWizard().getWereables().size();

        while (true){
            System.out.println("Select an item from the storage: ");
            System.out.println("\t0.- Exit");
            for(StorableItem s: demiurge.getHome().getStorage())
                System.out.println("\t" + position++ + ".- " + s.toString());
            System.out.print("So...: ");
            try {
                fromStorage = sn.nextInt();
                if(fromStorage == 0)
                    return;
                else if (fromStorage > 0 && fromStorage <= homeItems)
                    break;
                else
                    System.out.println("Select a number between 0 and " + homeItems);
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
        while (true){
            position = shift;
            System.out.println("Select one of your items: ");
            System.out.println("\t0.- Exit");
            for(StorableItem s: demiurge.getWizard().getWereables())
                System.out.println("\t" + position++ + ".- " + s.toString());
            System.out.print("So...: ");
            try {
                fromWizard = sn.nextInt();
                if(fromWizard == 0)
                    return;
                else if (fromWizard > 0 && fromWizard <= wizardItems)
                    break;
                else
                    System.out.println("Select a number between 0 and " + wizardItems);
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
        try {
            demiurge.storageExchangeItem(fromStorage - shift, fromWizard - shift);
        } catch (WereableIncompatibleException e) {
            System.out.println("Different types of items.");
        }
    }

    void menuDungeon() throws WizardTiredException, WizardDeathException {
        int option;
        demiurge.enterDungeon();

        System.out.println();
        System.out.println("-------");
        System.out.println("Dungeon");
        System.out.println("-------");
        while (true){
            System.out.println();
            System.out.println("Room: "+demiurge.getCurrentSite().toString());
            System.out.println();
            System.out.println("0.- Go to...");
            System.out.println("1.- Gather crystals.");
            System.out.println("2.- Search and manage items");
            if(demiurge.getCurrentSite().getCreature() != null)
                System.out.println("3.- Fight!");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> menuDungeonMove();
                    case 1 -> demiurge.gatherCrystals();
                    case 2 -> menuDungeonItems();
                    case 3 -> menuDungeonCreature();
                    default -> System.out.println("Select a number between 0 and 3");
                }
            } catch (CrystalCarrierFullException e) {
                System.out.println("No more crystals fit.");
            } catch (RoomCrystalEmptyException e) {
                System.out.println("The room has no crystals");
            } catch (HomeBackException e) {
                break;
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
    }

    void menuDungeonMove() throws HomeBackException, WizardTiredException {
        int option;
        int shift = 1;
        int position = shift;
        int totalDoors = demiurge.getCurrentSite().numberOfDoors();

        Site site;

        System.out.println();
        System.out.println("Select a door number: ");
        System.out.println("0.- I stay.");
        for(Door door: demiurge.getCurrentSite().getDoors()) {
            site = door.sniffFrom(demiurge.getCurrentSite());
            if (site instanceof Room)
                System.out.println(position++ + ".- (ID:" + ((Room)site).getID() + ") - " + ((Room)site).getDescription());
            else
                System.out.println(position++ + ".- HOME.");
        }

        try {
            option = sn.nextInt();
            if (option > 0 && option <= totalDoors)
                demiurge.openDoor(option-shift);
            else
                System.out.println("Select a number between 0 and " + totalDoors);
        } catch (InputMismatchException e) {
            System.out.println("Only numbers.");
            sn.next();
        }
    }

    void menuDungeonCreature() throws WizardDeathException, WizardTiredException {
        int option;
        Room room = demiurge.getCurrentSite();

        while (true){
            if(room.getCreature() == null){
                System.out.println("No creature in this room");
                break;
            }else if(room.getCreature().getLife() == 0){
                System.out.println("Creature killed");
                break;
            }

            System.out.println("UPS! there is a creature...");
            System.out.println("0.- Run");
            System.out.println("1.- Fight");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {
                        if(demiurge.canRunAway())
                            return;
                    }
                    case 1 -> {
                        if(demiurge.priority()) {
                            System.out.println("Wizard has priority");
                            if(demiurge.wizardAttack())
                                System.out.println("Wizard beats successfully");
                            if(demiurge.creatureAttack())
                                System.out.println("Creature beats successfully");
                        }else{
                            System.out.println("Creature has priority");
                            if(demiurge.creatureAttack())
                                System.out.println("Creature beats successfully");
                            if(demiurge.wizardAttack())
                                System.out.println("Wizard beats successfully");
                        }
                    }
                    default -> System.out.println("Select a number between 0 and 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            } catch (CreatureKilledException e) {
                System.out.println("Creature Killed");
            } catch (WizardNoWeaponException e) {
                System.out.println("No weapon... use spells or RUN AWAY!");
            }
        }
    }

    void menuDungeonItemsTake(){
        int option;
        int shift = 1;
        int position = shift;
        int siteItems = demiurge.getCurrentSite().getItems().size();

        while (true){
            if(siteItems == 0) {
                System.out.println("Nothing in this room.");
                break;
            }else{
                System.out.println("Select an item from room: ");
                System.out.println("\t0.- Exit");
                for(StorableItem s: demiurge.getCurrentSite().getItems())
                    System.out.println("\t" + position++ + ".- " + s.toString());
                System.out.print("So...: ");
                try {
                    option = sn.nextInt();
                    if(option == 0)
                        break;
                    else if (option > 0 && option <= siteItems) {
                        demiurge.roomTakeItem(option - shift);
                        break;
                    }else
                        System.out.println("Select a number between 0 and " + siteItems);
                } catch (WereablesFullException e) {
                    System.out.println("You can't put this item");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                    sn.next();
                }
            }
        }
    }

    void menuDungeonItemsExchange() {
        int fromRoom;
        int roomItems = demiurge.getCurrentSite().getItems().size();
        int wizardItems = demiurge.getWizard().getWereables().size();
        int fromWizard;
        int shift = 1;
        int position = shift;

        while (true){
            System.out.println("Select an item from room: ");
            System.out.println("\t0.- Exit");
            for(StorableItem s: demiurge.getCurrentSite().getItems())
                System.out.println("\t" + position++ + ".- " + s.toString());
            System.out.print("So...: ");
            try {
                fromRoom = sn.nextInt();
                if(fromRoom == 0)
                    return;
                else if (fromRoom > 0 && fromRoom <= roomItems)
                    break;
                else
                    System.out.println("Select a number between 0 and " + roomItems);
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
        while (true){
            position = shift;
            System.out.println("Select one of your items: ");
            System.out.println("\t0.- Exit");
            for(StorableItem s: demiurge.getWizard().getWereables())
                System.out.println("\t" + position++ + ".- " + s.toString());
            System.out.print("So...: ");
            try {
                fromWizard = sn.nextInt();
                if(fromWizard == 0)
                    return;
                else if (fromWizard > 0 && fromWizard <= wizardItems)
                    break;
                else
                    System.out.println("Select a number between 0 and " + wizardItems);
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
        try {
            demiurge.roomExchangeItem(fromRoom - shift, fromWizard - shift);
        } catch (WereableIncompatibleException e) {
            System.out.println("Different types of items.");
        }
    }

    void menuDungeonItems(){
        int option;

        while (true){
            if(demiurge.getCurrentSite().getItems().size() == 0){
                System.out.println("No items in the room");
                break;
            }
            System.out.println();
            System.out.print("Room items:");
            for(StorableItem s: demiurge.getCurrentSite().getItems())
                System.out.print(" " + s.toString());
            System.out.println();
            System.out.println("Wizard items:");
            for(StorableItem s: demiurge.getWizard().getWereables())
                System.out.print(" " + s.toString());
            System.out.println();
            System.out.println();
            System.out.println("0.- Exit");
            System.out.println("1.- Take item.");
            System.out.println("2.- Exchange items");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {return;}
                    case 1 -> menuDungeonItemsTake();
                    case 2 -> menuDungeonItemsExchange();
                    default -> System.out.println("Select a number between 0 and 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
                sn.next();
            }
        }
    }

    void showSleep(){
        System.out.println();
        System.out.println("Tomorrow will be day " + demiurge.getDay());
        System.out.println("Good night... zzz");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 21; j++) {
                if (j % 3 == 0)
                    System.out.print(".");
                else
                    System.out.print("zzz");
            }
            System.out.println("-");
        }
        System.out.println();
    }

}
