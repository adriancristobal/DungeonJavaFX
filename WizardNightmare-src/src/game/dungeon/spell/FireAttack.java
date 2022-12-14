package game.dungeon.spell;

import game.dungeon.Attack;
import game.dungeon.Domain;

public class FireAttack extends Spell implements Attack {

    public FireAttack() { super(Domain.FIRE, 1); }

    @Override
    public int getAttackValue() { return level.getValue(); }
}
