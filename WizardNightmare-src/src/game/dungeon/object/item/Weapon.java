package game.dungeon.object.item;

import game.dungeon.Attack;
import game.dungeon.Domain;

public class Weapon extends Item implements Attack {

    public Weapon(int v) { super(Domain.NONE, v); }

    @Override
    public int getAttackValue() { return value.getValue(); }

}
