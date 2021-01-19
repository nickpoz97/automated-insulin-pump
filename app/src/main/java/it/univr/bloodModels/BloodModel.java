package it.univr.bloodModels;

import it.univr.exceptions.LethalSugarValuesException;

public abstract class BloodModel {
    // boundaries
    private static final int maxSugar = 200;
    private static final int minSugar = 50;

    // dinamic data
    private int initialSugarLevel;
    private int actualSugarLevel;
    private int incrementRate;
    private int time = 0;

    public BloodModel(int initialSugarLevel, int incrementRate) throws LethalSugarValuesException {
        this.initialSugarLevel = initialSugarLevel;
        this.actualSugarLevel = initialSugarLevel;
        this.incrementRate = incrementRate;
        this.checkSugarValuesConsistency();
    }

    public BloodModel(){
        this((maxSugar + minSugar)/2, 0);
    }

    public int getInitialSugarLevel() {
        return initialSugarLevel;
    }

    private void checkSugarValuesConsistency() throws LethalSugarValuesException {
        if(this.actualSugarLevel < minSugar || this.actualSugarLevel > maxSugar){
            throw new LethalSugarValuesException(minSugar, maxSugar,this.actualSugarLevel);
        }
    }

    protected void updateSugarLevel() throws LethalSugarValuesException { // time independent and call number dependent
        this.time++;
        this.actualSugarLevel = this.initialSugarLevel + this.incrementRate * this.time;
        this.checkSugarValuesConsistency();
    }

    // only used for testing
    public int getSugarLevel(){
        return this.actualSugarLevel;
    }

    // injected insulin decreases increment rate
    public void injectInsulin(int amount){
        this.initialSugarLevel = this.actualSugarLevel;
        this.time = 0;
        this.incrementRate -= amount;
    }

    public void addSugar(int amount) {
        this.incrementRate += amount;
    }

    // different implementations
    public abstract int retrieveSugarLevel() throws LethalSugarValuesException;

    // only for testing purpose
    public static int getMaxSugar() {
        return maxSugar;
    }

    public static int getMinSugar() {
        return minSugar;
    }

    public int getIncrementRate() {
        return incrementRate;
    }
}
