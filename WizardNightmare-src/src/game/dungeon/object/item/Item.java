package game.dungeon.object.item;

import game.dungeon.Domain;
import game.util.Value;
import game.util.ValueOverMaxException;
import game.util.ValueUnderMinException;

public abstract class Item {

    Domain domain;
    Value value;

    public Item(Domain d, int val) {
        domain = d;
        value = new Value(val);
    }

    public Item(Domain d, int val, int min, int max) {
        domain = d;
        value = new Value(val, min, max);
    }

    public Domain getDomain(){ return domain; }

    public void updateValue(int v) { value.updateValue(v); }

    public int getValue() { return value.getValue(); }

    public boolean getBounded() { return value.getBounded(); }

    public int getMinimum() { return value.getMinimum(); }
    public int getMaximum() {
        return value.getMaximum();
    }

    public int availableToMinimum() { return value.availableToMinimum(); }
    public int availableToMaximum() { return value.availableToMaximum(); }

    public void setMinimum(int min) { value.setMinimum(min); }
    public void decreaseMinimum(int min) {
        value.decreaseMinimum(min);
    }

    public void setMaximum(int max) {
        value.setMaximum(max);
    }
    public void increaseMaximum(int max) {
        value.increaseMaximum(max);
    }

    public void setToMinimum() {
        value.setToMinimum();
    }
    public void setToMaximum() { value.setToMaximum(); }

    public void decreaseValue(int val) throws ValueUnderMinException { value.decreaseValue(val); }

    public void increaseValue(int val) throws ValueOverMaxException { value.increaseValue(val); }

    public String toString() {
        return this.getClass().getName().concat("[" + domain.toString() + ", " + value.toString() + "]");
    }

}
