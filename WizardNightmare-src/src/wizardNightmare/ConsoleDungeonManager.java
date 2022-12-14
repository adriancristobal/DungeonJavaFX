package wizardNightmare;

import game.character.exceptions.CreatureKilledException;
import game.character.exceptions.WizardDeathException;
import game.character.exceptions.WizardNoWeaponException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.DemiurgeDungeonManager;
import game.dungeon.Attack;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.spell.Knowledge;
import game.dungeon.structure.exceptions.HomeBackException;
import game.dungeon.structure.exceptions.RoomCrystalEmptyException;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class ConsoleDungeonManager {
    Scanner sn;
    DemiurgeDungeonManager dungeonManager;
    ConsoleContainerManager containerManager;

    ConsoleDungeonManager(Scanner s, DemiurgeDungeonManager dmm, ConsoleContainerManager ccm){
        sn = s;
        dungeonManager = dmm;
        containerManager = ccm;
    }

    void dungeon() throws WizardTiredException, WizardDeathException {
        int option;
        dungeonManager.enterDungeon();

        System.out.println();
        System.out.println("---------");
        System.out.println("-DUNGEON-");
        System.out.println("---------");
        while (true) {
            System.out.println();
            System.out.println("Room: " + dungeonManager.getRoomInfo());
            System.out.println();
            System.out.println("0.- Go to...");
            System.out.println("1.- Gather crystals.");
            System.out.println("2.- Manage items");
            if (dungeonManager.hasCreature())
                System.out.println("3.- Fight!");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> move();
                    case 1 -> dungeonManager.gatherCrystals();
                    case 2 -> containerManager.dungeon();
                    case 3 -> creature();
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
            } catch (ContainerFullException e) {
                System.out.println("No space!");
            } catch (RoomCrystalEmptyException e) {
                System.out.println("The room has no crystals");
            } catch (HomeBackException e) {
                break;
            }
        }
    }

    void move() throws HomeBackException, WizardTiredException {
        int doors = dungeonManager.getNumberOfDoors();
        int position = 1;
        int selection;
        Iterator it;

        System.out.println();
        System.out.println("Select a door number: ");
        System.out.println("\t0.- I stay.");

        it = dungeonManager.getDoorsIterator();
        while(it.hasNext())
            System.out.println("\t" + position++ + it.next().toString());
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection > 0 && selection <= doors)
                    dungeonManager.openDoor(selection - 1);
                else
                    System.out.println("Select a correct option.");
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }
    }

    void creature() throws WizardDeathException, WizardTiredException {
        int option;

        while (true) {
            if (!dungeonManager.isAlive()) {
                System.out.println("Creature killed!!!");
                break;
            }

            System.out.println("The creature is looking at you:");
            System.out.println("\t0.- Run");
            System.out.println("\t1.- Fight");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {
                        if (dungeonManager.canRunAway())
                            return;
                    }
                    case 1 -> {
                        if (dungeonManager.priority()) {
                            System.out.println("Wizard has priority");
                            if (dungeonManager.wizardAttack(selectAttack()))
                                System.out.println("Wizard beats successfully");
                            if (dungeonManager.creatureAttack())
                                System.out.println("Creature beats successfully");
                        } else {
                            System.out.println("Creature has priority");
                            if (dungeonManager.creatureAttack())
                                System.out.println("Creature beats successfully");
                            if (dungeonManager.wizardAttack(selectAttack()))
                                System.out.println("Wizard beats successfully");
                        }
                    }
                    default -> System.out.println("Select a number between 0 and 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
            } catch (WizardNoWeaponException e) {
                System.out.println("No weapon... use spells or RUN AWAY!");
            } catch (CreatureKilledException ignored) {
            }
        }
    }

    public Attack selectAttack(){
        int position = 0;
        int available = dungeonManager.getAttacksNumber();
        int selection;
        Iterator it;

        System.out.print("Select attack mode:");
        it = dungeonManager.getAttacks();
        while(it.hasNext())
            System.out.println("\t" + position++ + ".- " + it.next().toString());
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection < 0 || selection >  available)
                    System.out.println("Select a correct option.");
                else
                    return dungeonManager.getAttack(selection);
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }

    }

}
