package game.demiurge;

public class DungeonConfiguration {

    private int basicEnergyConsumption = 1;
    private int comfortModifierForEnergy = 10;

    private int basicIncrease = 1;
    private int stoneIncrease = 50;

    private int basicUpgradeCost = 10;
    private int comfortUpgradeCost = 100;

    private int singaPerLifePointCost = 3;

    //Crystal regeneration
    private int crystalsPerDay = 3;
    private int singaPerCrystal = 10;

    //Creature encounters
    private int minimumForRunAway = 30;
    private int fightSuccessHigh = 20;
    private int fightSuccessMedium = 50;
    private int fightSuccessLow = 80;



    public int getComfortModifierForEnergy() {
        return comfortModifierForEnergy;
    }

    public void setComfortModifierForEnergy(int comfortModifierForEnergy) {
        this.comfortModifierForEnergy = comfortModifierForEnergy;
    }

    public int getStoneIncrease() {
        return stoneIncrease;
    }

    public void setStoneIncrease(int stoneIncrease) {
        this.stoneIncrease = stoneIncrease;
    }

    public int getBasicIncrease() {
        return basicIncrease;
    }

    public void setBasicIncrease(int basicIncrease) {
        this.basicIncrease = basicIncrease;
    }

    public int getBasicUpgradeCost() {
        return basicUpgradeCost;
    }

    public void setBasicUpgradeCost(int basicUpgradeCost) { this.basicUpgradeCost = basicUpgradeCost; }

    public int getComfortUpgradeCost() {
        return comfortUpgradeCost;
    }

    public void setComfortUpgradeCost(int comfortUpgradeCost) {
        this.comfortUpgradeCost = comfortUpgradeCost;
    }

    public int getSingaPerLifePointCost() {
        return singaPerLifePointCost;
    }

    public void setSingaPerLifePointCost(int singaPerLifePointCost) { this.singaPerLifePointCost = singaPerLifePointCost; }

    public int getCrystalsPerDay() {
        return crystalsPerDay;
    }

    public void setCrystalsPerDay(int crystalsPerDay) {
        this.crystalsPerDay = crystalsPerDay;
    }

    public int getSingaPerCrystal() {
        return singaPerCrystal;
    }

    public void setSingaPerCrystal(int singaPerCrystal) {
        this.singaPerCrystal = singaPerCrystal;
    }

    public int getBasicEnergyConsumption() { return basicEnergyConsumption; }

    public void setBasicEnergyConsumption(int basicEnergyConsumption) { this.basicEnergyConsumption = basicEnergyConsumption; }

    public int getMinimumForRunAway() {
        return minimumForRunAway;
    }

    public void setMinimumForRunAway(int minimumForRunAway) {
        this.minimumForRunAway = minimumForRunAway;
    }

    public int getFightSuccessHigh() {
        return fightSuccessHigh;
    }

    public void setFightSuccessHigh(int fightSuccessHigh) {
        this.fightSuccessHigh = fightSuccessHigh;
    }

    public int getFightSuccessMedium() {
        return fightSuccessMedium;
    }

    public void setFightSuccessMedium(int fightSuccessMedium) {
        this.fightSuccessMedium = fightSuccessMedium;
    }

    public int getFightSuccessLow() {
        return fightSuccessLow;
    }

    public void setFightSuccessLow(int fightSuccessLow) {
        this.fightSuccessLow = fightSuccessLow;
    }

}

