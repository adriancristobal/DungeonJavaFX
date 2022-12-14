package game.character;

import game.character.exceptions.CreatureKilledException;
import game.dungeon.Domain;
import game.dungeon.PhysicalAttack;
import game.dungeon.spell.Spell;
import game.dungeon.spell.SpellUnknowableException;
import game.util.ValueUnderMinException;

public class Creature extends Character{

    private final Domain type;
    PhysicalAttack punch;


    public Creature(String n, int l, int p, Domain t) {
        super(n, l, l);
        type = t;
        punch = new PhysicalAttack(p);
        attacks.add(punch);
    }

    public boolean isAlive() { return getLife() <= 0;}

    public int randomAttack() { return attacks.get((int) (Math.random() * attacks.size())).getAttackValue(); }

    public void beat(int value) throws CreatureKilledException {
        try {
            life.decreaseValue(value);
        } catch (ValueUnderMinException e) {
            throw new CreatureKilledException();
        }
    }

    public void addSpell(Spell spell) throws SpellUnknowableException {
        if(spell.getType() == type)
            super.addSpell(spell);
        else
            throw new SpellUnknowableException();
    }

    public String toString() {
        String exit = name + "Type(" + type + ") Life(" + life + ") Punch(" + punch.getAttackValue() + ")";
        exit = exit.concat("\n\t\tSpells" + memory);
        return exit;
    }
}
