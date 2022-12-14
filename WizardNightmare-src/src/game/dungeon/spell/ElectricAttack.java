package game.dungeon.spell;

import game.dungeon.Attack;
import game.dungeon.Domain;

public class ElectricAttack extends Spell implements Attack {

    public ElectricAttack() { super(Domain.ELECTRICITY, 1); }

    @Override
    public int getAttackValue() { return level.getValue(); }

}
