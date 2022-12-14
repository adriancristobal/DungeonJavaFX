package game.dungeon.object.item;

import game.dungeon.Domain;
import game.dungeon.object.exceptions.ItemCreationErrorException;

public class Necklace extends Item {

    private Necklace(Domain d, int v) { super(d, v); }

    public static Necklace createNecklace(Domain d, int v) throws ItemCreationErrorException {
        if (d == Domain.ENERGY || d == Domain.LIFE)
            return new Necklace(d, v);
        else
            throw new ItemCreationErrorException();
    }
}
