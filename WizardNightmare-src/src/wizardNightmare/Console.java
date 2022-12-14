package wizardNightmare;

import game.character.exceptions.WizardDeathException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.Demiurge;
import game.demiurge.Interactive;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console implements Interactive {
    Scanner sn;
    Demiurge demiurge;
    ConsoleContainerManager containerManager;
    ConsoleHomeManager homeManager;
    ConsoleDungeonManager dungeonManager;


    public Console(Demiurge d) {
        sn = new Scanner(System.in);
        demiurge = d;
    }

    @Override
    public void configure() {
        System.out.println("--------------");
        System.out.println("-CONSOLE MODE-");
        System.out.println("--------------");
        System.out.println();
        System.out.println("Waking up Demiurge...");
        System.out.println("Preparing the dormant body suspension environment...");
        demiurge.loadEnvironment(new DungeonLoaderManual());
        containerManager = new ConsoleContainerManager(sn, demiurge.getContainerManager());
        homeManager = new ConsoleHomeManager(sn, demiurge.getHomeManager());
        dungeonManager = new ConsoleDungeonManager(sn, demiurge.getDungeonManager(), containerManager);
/*
        System.out.println("Set wizard's name: ");
        demiurge.setWizardName(sn.next());
        System.out.println();
        System.out.println("Waking up " + demiurge.getWizard().getName());
 */
    }

    public void start() {
        int option;

        while (true) {
            System.out.println();
            System.out.println("------");
            System.out.println("-HOME-");
            System.out.println("------");
            System.out.println(demiurge.homeInfo());
            System.out.println(demiurge.wizardInfo());
            System.out.println();
            System.out.println("Select: ");
            System.out.println("0.- Exit Game");
            System.out.println("1.- Sleep (recover energy)");
            System.out.println("2.- Recover life points");
            System.out.println("3.- Merge crystals");
            System.out.println("4.- Upgrade characteristics");
            System.out.println("5.- Manage Spells");
            System.out.println("6.- Manage storage");
            System.out.println("7.- Go to Dungeon");
            System.out.print("So...: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> {
                        System.out.println("Bye bye looser!");
                        return;
                    }
                    case 1 -> sleep();
                    case 2 -> homeManager.recover();
                    case 3 -> homeManager.mergeCrystals();
                    case 4 -> homeManager.upgrades();
                    case 5 -> homeManager.spells();
                    case 6 -> containerManager.home();
                    case 7 -> dungeonManager.dungeon();
                    default -> System.out.println("\nSelect a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (WizardTiredException e) {
                System.out.println("\nYou are very tired and an automatic spell has been cast.");
                sleep();
            } catch (WizardDeathException e) {
                System.out.println("\nYour are death!");
                return;
            }
        }
    }

    void sleep() {
        demiurge.nextDay();
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
