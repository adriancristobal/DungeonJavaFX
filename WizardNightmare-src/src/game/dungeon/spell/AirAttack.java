package game.dungeon.spell;

import game.dungeon.Attack;
import game.dungeon.Domain;

public class AirAttack extends Spell implements Attack {

    public AirAttack() { super(Domain.AIR, 1); }

    @Override
    public int getAttackValue() { return level.getValue(); }
}
