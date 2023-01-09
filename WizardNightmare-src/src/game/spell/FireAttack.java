package game.spell;

import game.actions.Attack;
import game.Domain;
import game.character.Character;
import game.character.exceptions.CharacterKilledException;

public class FireAttack extends Spell implements Attack {

    public FireAttack() { super(Domain.FIRE, 1); }

    @Override
    public int getDamage() { return level.getValue(); }

    @Override
    public void attack(Character character) throws CharacterKilledException {
        character.drainLife(character.protect(level.getValue(), domain));
    }
}
