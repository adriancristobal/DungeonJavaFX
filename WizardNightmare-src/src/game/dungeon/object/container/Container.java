package game.dungeon.object.container;

import game.dungeon.Domain;
import game.dungeon.object.item.Item;
import game.dungeon.object.exceptions.ContainerFullException;
import game.dungeon.object.exceptions.ContainerUnacceptedItemException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Basic aggregation of elements that is limited in capacity
 */
public abstract class Container extends Item {

    final ArrayList<Item> items;

    /**
     *
     * @param d type of domain
     * @param v capacity of the container
     */
    public Container(Domain d, int v) {
        super(d, v);
        items = new ArrayList<>();
    }

    /**
     *
     * @return number of items
     */
    public int size() { return items.size(); }

    /**
     * Check if there is space for more items
     * @param q the number of elements to be added
     * @return true if the given number fits
     */
    public boolean fit(int q){ return getValue()>q+items.size(); }

    /**
     * Check if the storage is full
     * @return true if the storage is full
     */
    public boolean full() { return getValue()==items.size(); }

    /**
     * Allows the iteration of the collection
     * @return the iterator
     */
    public Iterator iterator(){ return items.iterator(); }

    public Item get(int index){ return items.get(index); }

    /**
     * Adds an element to the collection
     * @param i the element to be added
     * @throws ContainerFullException the container is full
     * @throws ContainerUnacceptedItemException the container don't accept that item
     */
    public void add(Item i) throws ContainerFullException, ContainerUnacceptedItemException {
        if(full())
            throw new ContainerFullException();
        else
            items.add(i);
    }

    public Item remove(int index){ return items.remove(index); }


    /**
     * A list of items to be added
     * @param c a list of itema to be added
     */
    private void addAll(ArrayList<Item> c) { items.addAll(c); }

    /**
     * Allows to exchange between to collections if there is space
     * @param s collection where items will be added
     * @throws ContainerFullException
     */
    public void exchange(Container s) throws ContainerFullException {
        if (s.fit(items.size())){
            s.addAll(items);
            items.clear();
        }else
            throw new ContainerFullException();
    }

    public String toString() {
        String exit = super.toString() + "->[";
        for (Item i : items)
            exit = exit.concat(i.toString() + ";");
        exit = exit.concat("]");
        return exit;
    }

}
