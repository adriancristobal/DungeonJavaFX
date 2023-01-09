package console;

import game.actions.Attack;
import game.demiurge.exceptions.EndGameException;
import game.demiurge.exceptions.ExitException;
import game.demiurge.exceptions.GoHomekException;
import game.dungeon.Door;
import game.character.exceptions.CharacterKilledException;
import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.DemiurgeDungeonManager;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class ConsoleDungeonManager {
    Scanner sn;
    DemiurgeDungeonManager dDungeonManager;
    ConsoleContainerManager cContainerManager;

    ConsoleDungeonManager(Scanner s, DemiurgeDungeonManager dmm, ConsoleContainerManager ccm){
        sn = s;
        dDungeonManager = dmm;
        cContainerManager = ccm;
    }

    void dungeon() throws WizardTiredException, CharacterKilledException, GoHomekException, ExitException, EndGameException {
        int option;
        dDungeonManager.enterDungeon();

        System.out.println();
        System.out.println("---------");
        System.out.println("-DUNGEON-");
        System.out.println("---------");
        while (true) {
            System.out.println();
            System.out.println("Room: " + dDungeonManager.getRoomInfo());
            System.out.println();
            System.out.println(dDungeonManager.wizardInfo());
            System.out.println();
            System.out.println("0.- Exit Game");
            System.out.println("1.- Go to...");
            System.out.println("2.- Gather crystals.");
            System.out.println("3.- Manage items.");
            if (dDungeonManager.hasCreature())
                if(dDungeonManager.isAlive())
                    System.out.println("4.- Face creature.");
                else
                    System.out.println("The creature is death.");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> throw new ExitException();
                    case 1 -> move();
                    case 2 -> dDungeonManager.gatherCrystals();
                    case 3 -> {
                        cContainerManager.dungeon();
                        dDungeonManager.checkWeapon();
                    }
                    case 4 -> {
                        if (dDungeonManager.hasCreature() && dDungeonManager.isAlive())
                            creature();
                    }
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
            }
        }
    }

    void move() throws GoHomekException, WizardTiredException, EndGameException {
        int doors = dDungeonManager.getNumberOfDoors();
        int position = 1;
        int selection;
        Iterator it;

        System.out.println();
        System.out.println("Select a door number: ");
        System.out.println("\t0.- I stay.");

        it = dDungeonManager.getDoorsIterator();
        while(it.hasNext())
            System.out.println("\t" + position++ + ".- " + dDungeonManager.showOtherSite((Door) it.next()));
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection > 0 && selection <= doors) {
                    dDungeonManager.openDoor(selection - 1);
                    return;
                }else
                    System.out.println("Select a correct option.");
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }
    }

    void creature() throws WizardTiredException, CharacterKilledException {
        int option;

        System.out.println();
        while (true) {
            if (!dDungeonManager.isAlive()) {
                System.out.println("Creature killed!!!");
                break;
            }

            System.out.println();
            System.out.println(dDungeonManager.wizardLifeInfo());
            System.out.println(dDungeonManager.creatureLifeInfo());
            System.out.println("The creature is looking at you:");
            System.out.println("\t0.- Run.");
            System.out.println("\t1.- Attack.");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {
                        if (dDungeonManager.canRunAway())
                            return;
                        else {
                            System.out.println("The creature has an opportunity attack.");
                            if (dDungeonManager.creatureAttack())
                                System.out.println("Creature beats successfully");
                        }
                    }
                    case 1 -> {
                        if (dDungeonManager.priority()) {

                            System.out.println("Wizard has priority");
                            try {
                                if (dDungeonManager.wizardAttack(selectAttack()))
                                    System.out.println("Wizard beats successfully");
                            } catch (CharacterKilledException e) {
                                System.out.println("You have killed the creature");
                            }
                            if (dDungeonManager.creatureAttack())
                                System.out.println("Creature beats successfully");

                        } else {

                            System.out.println("Creature has priority");
                            if (dDungeonManager.creatureAttack())
                                System.out.println("Creature beats successfully");
                            try {
                                if (dDungeonManager.wizardAttack(selectAttack()))
                                    System.out.println("Wizard beats successfully");
                            } catch (CharacterKilledException e) {
                                System.out.println("You have killed the creature");
                            }

                        }
                    }
                    default -> System.out.println("Select a number between 0 and 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Only numbers.");
            } catch (WizardNotEnoughEnergyException e) {
                System.out.println("Not enough energy for that attack.");
            }
        }
    }



    public Attack selectAttack(){
        int position = 0;
        int available = dDungeonManager.getNumberOfAttacks();
        int selection;
        Iterator it;

        System.out.println("Select attack mode:");
        it = dDungeonManager.getAttacksIterator();
        while(it.hasNext())
            System.out.println("\t" + position++ + ".- " + it.next().toString());
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection < 0 || selection >  available)
                    System.out.println("Select a correct option.");
                else
                    return dDungeonManager.getAttack(selection);
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }

    }

}
