package game.dungeon;

import game.dungeon.Attack;

public class PhysicalAttack implements Attack {

    int value;

    public PhysicalAttack(int value){ this.value = value; }

    @Override
    public int getAttackValue() { return value; }
}
