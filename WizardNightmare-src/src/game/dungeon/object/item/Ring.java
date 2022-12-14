package game.dungeon.object.item;

import game.dungeon.Domain;
import game.dungeon.object.exceptions.ItemCreationErrorException;

public class Ring extends Item {

    private Ring(Domain d, int v) { super(d, v); }

    public static Ring createRing(Domain d, int v) throws ItemCreationErrorException {
        if (d == Domain.ELECTRICITY || d == Domain.FIRE || d == Domain.AIR)
            return new Ring(d, v);
        else
            throw new ItemCreationErrorException();
    }
}
