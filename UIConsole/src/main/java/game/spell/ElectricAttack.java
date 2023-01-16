package game.spell;

import game.Domain;
import game.actions.Attack;
import game.character.Character;
import game.character.exceptions.CharacterKilledException;

public class ElectricAttack extends Spell implements Attack {

    public ElectricAttack() { super(Domain.ELECTRICITY, 1); }

    @Override
    public int getDamage() { return level.getValue(); }

    @Override
    public void attack(Character character) throws CharacterKilledException {
        character.drainLife(character.protect(level.getValue(), domain));
    }

}
