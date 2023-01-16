package game.actions;

import game.character.Character;
import game.character.exceptions.CharacterKilledException;

public interface Attack {
    public int getDamage();

    public void attack(Character character) throws CharacterKilledException;
}
