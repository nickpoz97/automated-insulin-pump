package it.univr.bloodModels;

import it.univr.exceptions.LethalSugarValuesException;

public abstract class BloodModel {
    // boundaries
    private static final int maxSugar = 150;
    private static final int minSugar = 50;

    // dinamic data
    private int sugarLevel;
    private int incrementValue;
    private int incrementRate;

    // instance detail flag
    private boolean isInterative;

    public BloodModel(int sugarLevel, int incrementValue, int incrementRate) throws LethalSugarValuesException {
        this.sugarLevel = sugarLevel;
        this.incrementValue = incrementValue;
        this.incrementRate = incrementRate;
        this.checkSugarValuesConsistency();
    }

    private void checkSugarValuesConsistency() throws LethalSugarValuesException {
        if(this.sugarLevel < minSugar || this.sugarLevel > maxSugar){
            throw new LethalSugarValuesException(minSugar, maxSugar,this.sugarLevel);
        }
    }

    protected void updateSugarLevel() throws LethalSugarValuesException { // time independent and call number dependent
        this.sugarLevel += this.incrementValue;
        this.incrementValue += incrementRate;
        this.checkSugarValuesConsistency();
    }

    protected int getSugarLevel(){
        return this.sugarLevel;
    }

    // injected insulin decreases increment rate
    public void injectInsulin(int amount){ this.incrementRate -= amount; }

    public void addSugar(int amount) throws LethalSugarValuesException{
        this.sugarLevel += amount;
        this.checkSugarValuesConsistency();
    }

    protected void setInteractive(boolean interactive) {
        isInterative = interactive;
    }

    // different implementations
    public abstract int actualSugarLevel() throws LethalSugarValuesException;

    public boolean isInterative() {
        return isInterative;
    }

    // only for testing purpose
    public static int getMaxSugar() {
        return maxSugar;
    }

    public static int getMinSugar() {
        return minSugar;
    }

    public int getIncrementValue() {
        return incrementValue;
    }

    public int getIncrementRate() {
        return incrementRate;
    }
}
