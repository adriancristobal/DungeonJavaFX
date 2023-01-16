import console.Console;
import game.Interactive;
import game.demiurge.Demiurge;


/**
 *      The "Very High, Very Ancient and Very Well Tempered Supreme Council of the Arcane Magical Sciences" has created the
 * Ultimate prison for wayward wizards.
 *      This prison, called "The Magician's Nightmare", consists of putting the rebellious magician in a state of bodily suspension
 * latent with total suppression of powers, while his mind, in active suspension, is introduced into a simulation
 * dreamlike.
 *      This simulation corresponds to a dungeon from which you must leave if you want to regain your freedom. but bliss
 * dungeon is full of dangers and death in it also means real death. These hazards are creatures that
 * represent the errors and defects of the magician that he himself has to annihilate.
 * Every wizard who makes it out alive will have gone through a transformative re-education and reconversion process that will
 * You will return to the real world as a good profit magician.
 *      Very well, you have been a bad magician, and you have been sentenced to…
 * ---- THE WIZARD'S NIGHTMARE ----
 *      Now you'll have to get awake
 *
 */
public class WizardNightmare {
    public static void main(String[] args) {
        Demiurge demiurge = new Demiurge();
        Interactive console = new Console(demiurge);
        console.configure();
        console.start();
    }
}