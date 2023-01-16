package game.spell;

import game.Domain;
import game.actions.Attack;
import game.character.Character;
import game.character.exceptions.CharacterKilledException;

public class AirAttack extends Spell implements Attack {

    public AirAttack() { super(Domain.AIR, 1); }

    @Override
    public int getDamage() { return level.getValue(); }

    @Override
    public void attack(Character character) throws CharacterKilledException {
        character.drainLife(character.protect(level.getValue(), domain));
    }
}
