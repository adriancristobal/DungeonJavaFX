package wizardNightmare;

import game.character.exceptions.WizardNotEnoughEnergyException;
import game.character.exceptions.WizardSpellKnowedException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.DemiurgeHomeManager;
import game.dungeon.object.container.Container;
import game.dungeon.object.exceptions.ContainerEmptyException;
import game.dungeon.object.exceptions.ContainerErrorException;
import game.dungeon.spell.Knowledge;
import game.dungeon.structure.exceptions.HomeNotEnoughSingaException;
import game.util.ValueOverMaxException;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import static wizardNightmare.DungeonConfigValues.SINGA_PER_LIFEPOINT_COST;

public class ConsoleHomeManager {
    Scanner sn;
    DemiurgeHomeManager homeManager;

    ConsoleHomeManager(Scanner s, DemiurgeHomeManager dum){
        sn = s;
        homeManager = dum;
    }

    void recover() throws WizardTiredException {
        int points;

        while (true) {
            System.out.println();
            System.out.println("----------------------");
            System.out.println("Recovering life points");
            System.out.println("----------------------");
            if (homeManager.fullOfLife()) {
                System.out.println("You are full of life points.");
                break;
            } else {
                System.out.println("You have " + homeManager.getLife() + " of " + homeManager.getLifeMax() + " points of life.");
                System.out.println("There is " + homeManager.getSinga() + " singa and recovering each life point costs " + SINGA_PER_LIFEPOINT_COST + " singa.");
                System.out.print("How many points do you want to recover (0 to exit)? ");
                try {
                    points = sn.nextInt();
                    if (points == 0)
                        break;
                    else
                        homeManager.recover(points);
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                } catch (HomeNotEnoughSingaException e) {
                    System.out.println("You don't have enough singa.");
                } catch (ValueOverMaxException e) {
                    System.out.println("Too many life points.");
                }
            }
        }
    }

    void mergeCrystals() throws WizardTiredException {
        Container carrier = homeManager.getCarrier();
        int shift = 1;
        int crystals;
        int option;
        int singaSpace;

        while (true) {
            crystals = carrier.size();
            singaSpace = homeManager.getSingaSpace();
            System.out.println();
            System.out.println("----------------");
            System.out.println("Merging crystals");
            System.out.println("----------------");
            if (crystals == 0) {
                System.out.println("No crystals to merge.");
                break;
            } else if (singaSpace == 0) {
                System.out.println("Your singa storage is full of singa.");
                break;
            } else {
                try {
                    System.out.println("There is space to store: " + singaSpace + " singa.");
                    System.out.println("0.- End merge.");
                    int pos = shift;
                    Iterator it = carrier.iterator();
                    while(it.hasNext())
                        System.out.println(pos++ + ".- To merge crystal with " + it.next().toString() + " singa.");
                    System.out.print("So...: ");
                    option = sn.nextInt();
                    if (option == 0)
                        break;
                    else if (option > 0 && option <= crystals)
                        homeManager.mergeCrystal(option - shift);
                    else
                        System.out.println("Select a correct option.");
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers.");
                } catch (ContainerEmptyException e) {
                    System.out.println("No crystals to merge");
                    break;
                } catch (ContainerErrorException e) {
                    System.out.println("Impossible to merge crystals");
                    break;
                }
            }
        }
    }

    public void upgrades() throws WizardTiredException {
        int option;

        while (true) {
            System.out.println();
            System.out.println("---------");
            System.out.println("-Upgrade-");
            System.out.println("---------");
            System.out.println();
            System.out.println("Select: ");
            System.out.println("0.- Exit");
            System.out.println("1.- Upgrade max life");
            System.out.println("2.- Upgrade max energy");
            System.out.println("3.- Upgrade home comfort");
            System.out.println("4.- Upgrade stone capacity");
            System.out.print("So...: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> { return; }
                    case 1 -> homeManager.upgradeLifeMax();
                    case 2 -> homeManager.upgradeEnergyMax();
                    case 3 -> homeManager.upgradeComfort();
                    case 4 -> homeManager.upgradeSingaMax();
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (HomeNotEnoughSingaException e) {
                System.out.println("\nYou don't have enough singa.");
            } catch (WizardNotEnoughEnergyException e) {
                System.out.println("\nYou don't have enough energy to do that.");
            }
        }
    }

    public void spells() throws WizardTiredException {
        int option;

        while (true) {
            System.out.println();
            System.out.println("--------");
            System.out.println("-Spells-");
            System.out.println("--------");
            System.out.println();
            System.out.println("Select: ");
            System.out.println("0.- Exit");
            System.out.println("1.- Improve a known spell");
            System.out.println("2.- Learn new spell");
            System.out.print("So...: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> { return; }
                    case 1 -> improveSpell();
                    case 2 -> learnSpell();
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (HomeNotEnoughSingaException e) {
                System.out.println("\nYou don't have enough singa.");
            } catch (WizardNotEnoughEnergyException e) {
                System.out.println("\nYou don't have enough energy to do that.");
            } catch (WizardSpellKnowedException e) {
                System.out.println("\nSpell already known.");
            }
        }

    }

    private void improveSpell() throws WizardTiredException, HomeNotEnoughSingaException, WizardNotEnoughEnergyException {
        int option;

        System.out.println();
        System.out.println("Select an spell to improve: ");
        option = selectSpell(homeManager.getMemory());

        if (option == 0)
            return;

        try {
            homeManager.improveSpell(option);
        } catch (ValueOverMaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void learnSpell() throws WizardTiredException, HomeNotEnoughSingaException, WizardNotEnoughEnergyException, WizardSpellKnowedException {
        int option;

        System.out.println();
        System.out.println("Select an spell to learn: ");
        option = selectSpell(homeManager.getLibrary());

        if (option == 0)
            return;

        homeManager.learnSpell(option);
    }

    int selectSpell(Knowledge knowledge){
        int position = 1;
        int available = knowledge.size();
        int selection;
        Iterator it;

        System.out.print("Select an spell:");
        System.out.println("\t0.- Exit");
        it = knowledge.iterator();
        while(it.hasNext())
            System.out.println("\t" + position++ + ".- " + it.next().toString());
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection < 0 || selection >  available)
                    System.out.println("Select a correct option.");
                else
                    return selection - 1;
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }
    }
}
