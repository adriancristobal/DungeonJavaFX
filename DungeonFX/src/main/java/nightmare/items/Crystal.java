package nightmare.items;

public class Crystal {
    private int singa;

    public static Crystal createCrystal(int maximum){
        return new Crystal((int)(Math.random()*maximum));
    }
    public Crystal(int m){
        singa = m;
    }
    public int getSinga() {
        return singa;
    }
}
