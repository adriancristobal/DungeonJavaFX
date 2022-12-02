package nightmare;

import nightmare.management.Console;
import nightmare.management.DungeonLoaderManual;
import nightmare.management.Demiurge;

public class WizardNightmare {
    public static void main(String[] args) {
        Demiurge demiurge = new Demiurge(new DungeonLoaderManual());
        Console console = new Console(demiurge);
        console.start();
    }
}