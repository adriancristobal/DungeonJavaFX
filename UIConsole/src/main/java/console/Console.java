package console;

import game.Interactive;
import game.character.exceptions.CharacterKilledException;
import game.character.exceptions.WizardTiredException;
import game.demiurge.Demiurge;
import game.demiurge.exceptions.*;
import loaderManual.DungeonLoaderManual;

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
        System.out.println("Waking up Demiurge...");
        System.out.println("Preparing the dormant body suspension environment...");
        demiurge.loadEnvironment(new DungeonLoaderManual());
        containerManager = new ConsoleContainerManager(sn, demiurge.getContainerManager());
        homeManager = new ConsoleHomeManager(sn, demiurge.getHomeManager(), containerManager);
        dungeonManager = new ConsoleDungeonManager(sn, demiurge.getDungeonManager(), containerManager);
/*
        System.out.println("Set wizard's name: ");
        demiurge.setWizardName(sn.next());
        System.out.println();
        System.out.println("Waking up " + demiurge.getWizard().getName());
 */
    }

    public void start(){
        boolean atHome = true;

        while (true) {
            try {
                if (atHome)
                    homeManager.home();
                else
                    dungeonManager.dungeon();
            } catch (GoDungeonException e) {
                atHome = false;
            } catch (GoHomekException e) {
                atHome = true;
            } catch (CharacterKilledException e) {
                System.out.println("GAME OVER");
                return;
            } catch (WizardTiredException | GoSleepException e) {
                atHome = true;
                System.out.println("Good night... zZzZzZzz");
                System.out.println(".");
                System.out.println(".");
                System.out.println(".");
                demiurge.nextDay();
                System.out.println("Good morning!\nDay: " + demiurge.getDay());
            } catch (ExitException e) {
                //¿Save game?
                System.out.println("Bye bye");
                return;
            } catch (EndGameException e) {
                System.out.println();
                System.out.println("Congratulations!");
                System.out.println();
                System.out.println("You have escaped from the dungeon and you are a new and renewed wizard.");
                System.out.println("Now you can go back to your normal life.");
                System.out.println();
                System.out.println();
                System.out.println("Bye bye");
                return;
            }
        }
    }

}
