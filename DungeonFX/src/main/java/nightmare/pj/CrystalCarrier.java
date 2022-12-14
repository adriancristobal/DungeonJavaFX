package nightmare.pj;

import nightmare.items.Crystal;

import java.util.ArrayList;

public class CrystalCarrier {
    private int capacity;
    private ArrayList<Crystal> crystals;
    
    public CrystalCarrier(int c){
        capacity = c;
        crystals = new ArrayList<>();
    }

    public int getCapacity(){
        return capacity;
    }

    public ArrayList<Crystal> getCrystals(){
        return crystals;
    }

    public boolean isFull(){
        return capacity == crystals.size();
    }

    public ArrayList<Crystal> removeAll(){
        crystals.clear();
        return crystals;
    }

    public void addAll(ArrayList<Crystal> c){
        crystals.addAll(c);
    }

    public void add(Crystal c){
        crystals.add(c);
    }

    public int changeToSmallest(Crystal c){
        int exit = 0;
        Crystal smallest = crystals.get(0);
        for (int i = 1; i< crystals.size(); i++){
             if (crystals.get(i).getSinga() < smallest.getSinga())
                 smallest = crystals.get(i);
        }
        if (c.getSinga() > smallest.getSinga()) {
            crystals.remove(smallest);
            crystals.add(c);
            exit = 1;
        }
        return exit;
    }

    public String toString(){
        int number = 0;
        int singa = 0;
        for(Crystal c: crystals){
            number++;
            singa += c.getSinga();
        }
        return "[" + capacity + "(" + number + ":" + singa + ")]";
    }


}
