package nightmare.util;

public class AttributeControl {
    int currentValue;
    int maxValue;

    public AttributeControl(int c, int m){
        currentValue = c;
        maxValue = m;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setToMax(){
        currentValue = maxValue;
    }

    public void increaseCurrentValue(int c) throws AttributeException {
        if (currentValue + c > maxValue)
            throw new AttributeException();
        else
            currentValue += c;
    }

    public void decreaseCurrentValue(int c) throws AttributeException {
        if (currentValue - c < 0)
            throw new AttributeException();
        else
            currentValue -= c;
    }

    public void upgradeMaxValue(int m){
        maxValue += m;
    }

    public void reset() {
        currentValue = 0;
    }

    public int getDifference() {
        return maxValue - currentValue;
    }

    public String toString(){
        return new String("("+currentValue+":"+maxValue+")");
    }


}
