package nightmare.items;

import nightmare.spells.Magic_Type;

public class StorableItem {

    Magic_Type type;
    int value;

    StorableItem(Magic_Type t, int v){
        type = t;
        value = v;
    }

    public Magic_Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String toString(){
        String item = this.getClass().toString();
        return new String(item.substring(item.lastIndexOf(".")+1)+"("+type.toString()+":"+value+")");
    }
}
