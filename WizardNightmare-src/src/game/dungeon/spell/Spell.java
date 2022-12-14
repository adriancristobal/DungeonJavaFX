package game.dungeon.spell;

import game.dungeon.Domain;
import game.util.Value;
import game.util.ValueOverMaxException;

public abstract class Spell {
    Domain type;
    Value level;

    Spell (Domain type, int level){
        this.type = type;
        this.level = new Value(level, 1, 5);
    }

    Spell (Spell spell){ this(spell.type, spell.getLevel()); }

    public Domain getType() { return type; }

    public int getLevel () { return level.getValue(); }

    public void improve() throws ValueOverMaxException { level.increaseValue(1);}

    public String toString(){
        return getClass().getName() + "[" + type + ":" + level + "]";
    }

}
