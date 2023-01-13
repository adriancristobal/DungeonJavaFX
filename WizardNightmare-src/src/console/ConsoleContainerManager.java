package console;

import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerInvalidExchangeException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.objectContainer.Container;
import game.demiurge.DemiurgeContainerManager;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class ConsoleContainerManager {
    Scanner sn;
    DemiurgeContainerManager containerManager;

    ConsoleContainerManager(Scanner s, DemiurgeContainerManager dim){
        sn = s;
        containerManager = dim;
    }

    void home() {
        int option;

        while (true) {
            System.out.println();
            System.out.println("-------");
            System.out.println("Storage");
            System.out.println("-------");
            System.out.println();
            System.out.println(containerManager.getWearables());
            System.out.println(containerManager.getBag());
            System.out.println(containerManager.getSite());
            System.out.println();
            System.out.println("0.- Exit");
            System.out.println("1.- Delete from storage (item will disappear)");
            System.out.println("2.- Delete from jewelry bag (item will disappear)");
            System.out.println("3.- Take from storage");
            System.out.println("4.- Take from jewelry bag");
            System.out.println("5.- Leave in the chest");
            System.out.println("6.- Leave in the jewelry bag");
            System.out.println("7.- Exchange between wizard and chest");
            System.out.println("8.- Exchange between wizard and jewelry bag");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> { return; }
                    case 1 -> menuContainerDelete(containerManager.getSite());
                    case 2 -> menuContainerDelete(containerManager.getBag());
                    case 3 -> menuContainerAdd(containerManager.getSite(), containerManager.getWearables());
                    case 4 -> menuContainerAdd(containerManager.getBag(), containerManager.getWearables());
                    case 5 -> menuContainerAdd(containerManager.getWearables(), containerManager.getSite());
                    case 6 -> menuContainerAdd(containerManager.getWearables(), containerManager.getBag());
                    case 7 -> menuContainerExchange(containerManager.getWearables(), containerManager.getSite());
                    case 8 -> menuContainerExchange(containerManager.getWearables(), containerManager.getBag());
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (ContainerUnacceptedItemException e) {
                System.out.println("\nItem not valid.");
            } catch (ContainerFullException e) {
                System.out.println("\nThere is no space.");
            }
        }
    }

    void dungeon() {
        int option;

        while (true) {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("-Room Items Management-");
            System.out.println("-----------------------");
            System.out.println();
            System.out.println(containerManager.getWearables());
            System.out.println(containerManager.getBag());
            System.out.println(containerManager.getSite());
            System.out.println();
            System.out.println("0.- Exit");
            System.out.println("1.- Delete from jewelry bag (item will disappear)");
            System.out.println("2.- Take from room");
            System.out.println("3.- Take from jewelry bag");
            System.out.println("4.- Drop in room");
            System.out.println("5.- Leave in jewelry bag");
            System.out.println("6.- Exchange between wizard and room");
            System.out.println("7.- Exchange between wizard and jewelry bag");
            System.out.print("So...: ");

            try {
                option = sn.nextInt();
                switch (option) {
                    case 0 -> { return; }
                    case 1 -> menuContainerDelete(containerManager.getBag());
                    case 2 -> menuContainerAdd(containerManager.getSite(), containerManager.getWearables());
                    case 3 -> menuContainerAdd(containerManager.getBag(), containerManager.getWearables());
                    case 4 -> menuContainerAdd(containerManager.getWearables(), containerManager.getSite());
                    case 5 -> menuContainerAdd(containerManager.getWearables(), containerManager.getBag());
                    case 6 -> menuContainerExchange(containerManager.getWearables(), containerManager.getSite());
                    case 7 -> menuContainerExchange(containerManager.getWearables(), containerManager.getBag());
                    default -> System.out.println("Select a correct option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nOnly numbers.");
            } catch (ContainerUnacceptedItemException e) {
                System.out.println("\nItem not valid.");
            } catch (ContainerFullException e) {
                System.out.println("\nThere is no space.");
            }
        }
    }

    void menuContainerDelete(Container c) {
        int cItem = selectItem(c);
        if (cItem == 0)
            return;

        containerManager.deleteItem(c, cItem);
    }

    void menuContainerAdd(Container source, Container receptor) throws ContainerUnacceptedItemException, ContainerFullException {

        int sourceItem = selectItem(source);
        if (sourceItem == 0)
            return;
        containerManager.addItem(source, sourceItem, receptor);
    }

    void menuContainerExchange(Container a, Container b) {
        int aItem = selectItem(a);
        if (aItem == 0)
            return;
        int bItem = selectItem(b);
        if (bItem == 0)
            return;

        try {
            containerManager.exchangeItem(a, aItem, b,bItem);
        } catch (ContainerInvalidExchangeException e) {
            System.out.println("Different types of items.");
        }
    }

    int selectItem(Container container){
        int available = container.size();
        int selection;
        int position = 1;
        Iterator it;

        System.out.print("Select an item from " + container.getClass().getName() + ":");
        System.out.println("\t0.- Exit");
        it = container.iterator();
        while(it.hasNext())
            System.out.println("\t" + position++ + ".- " + it.next().toString());
        System.out.print("So...: ");

        while (true) {
            try {
                selection = sn.nextInt();
                if (selection < 0 || selection >  available)
                    System.out.println("Select a correct option.");
                else
                    return selection-1;
            } catch (InputMismatchException e) {
                System.out.println("\n\nOnly numbers.\n");
            }
        }
    }


}
