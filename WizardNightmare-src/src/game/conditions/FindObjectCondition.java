package game.conditions;

import game.character.Creature;
import game.object.Item;

public class FindObjectCondition implements Condition{
    Item item;

    public FindObjectCondition(Item item) { this.item = item; }

    @Override
    public boolean check() { return item.isViewed(); }
}
