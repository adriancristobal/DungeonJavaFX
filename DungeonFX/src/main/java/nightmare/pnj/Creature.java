package nightmare.pnj;

import nightmare.spells.Magic_Type;
import nightmare.spells.Spell;

import java.util.ArrayList;

public class Creature {

    private int life;
    private int punch;
    private Magic_Type type;
    private ArrayList<Spell> spells;

    public Creature(Magic_Type t, int l, int p){
        life = l;
        type = t;
        punch = p;
        spells = new ArrayList<>();
    }

    public int getLife() {
        return life;
    }

    public int getPunch(){
        return punch;
    }

    public void beat(int value) throws CreatureKilledException {
        life -= value;
        if(life <= 0)
            throw new CreatureKilledException();
    }

    public String toString (){
        String exit = "Type(" + type + ") Life(" + life + ") Punch(" + punch +")";
        exit = exit.concat("\n\t\tSpells"+spells);
        return exit;
    }
}
