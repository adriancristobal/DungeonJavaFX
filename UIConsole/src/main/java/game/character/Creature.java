package game.character;

import game.Domain;
import game.actions.Attack;
import game.spell.Spell;
import game.spell.SpellUnknowableException;

public class Creature extends Character {

    private boolean viewed = false;

    public Creature(String n, int life, int hit, Domain t) { super(n, t, life, life, hit); }

    public boolean isAlive() { return getLife() > 0;}

    public boolean isViewed() { return viewed;}
    public void view() { viewed = true; }

    public Attack getRandomAttack() { return  getAttack((int) (Math.random() * attacks.size())); }

    public int protect(int damage, Domain d){
        int protection = 1;
        if (domain == d)
            protection /= 2;
        return damage * protection;
    }

    public void addSpell(Spell spell) throws SpellUnknowableException {
        if(spell.getDomain() == domain)
            super.addSpell(spell);
        else
            throw new SpellUnknowableException();
    }

    public String toString() {
        return name + "\tType(" + domain + ")\tLife(" + life + ")\tPunch(" + attacks.get(0).getDamage() + ")"
                + "\n\t" + memory;

    }

}
