package game.dungeon.object.item;

import game.dungeon.Domain;

public class SingaCrystal extends Item{

    private SingaCrystal(int s) { super(Domain.NONE, s); }

    public static SingaCrystal createCrystal(int maximum) {
        return new SingaCrystal((int) (Math.random() * maximum));
    }
}
