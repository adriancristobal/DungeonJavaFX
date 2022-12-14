package nightmare.items;

import nightmare.spells.Magic_Type;

public class Necklace extends StorableItem{

    public Necklace(Magic_Type t, int v){
        super(t, v);
    }

    public void consume(int val){
        value -= val;
    }
}
