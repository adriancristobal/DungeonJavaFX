package game.object;

import game.actions.Attack;
import game.character.Character;
import game.Domain;
import game.character.exceptions.CharacterKilledException;

public class Weapon extends Item implements Attack {

    public Weapon(int v) { super(Domain.NONE, v); }

    @Override
    public int getDamage() { return value.getValue(); }

    @Override
    public void attack(Character character) throws CharacterKilledException {
        character.drainLife(value.getValue());
    }

}
